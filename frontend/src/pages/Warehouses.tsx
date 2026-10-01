import { useEffect, useState } from 'react'

interface Warehouse {
  id: number
  name: string
  code: string
  address: string
  active: boolean
}

interface Location {
  id: number
  name: string
  code: string
  description: string
  active: boolean
}

function Warehouses() {
  const [warehouses, setWarehouses] = useState<Warehouse[]>([])
  const [locations, setLocations] = useState<Record<number, Location[]>>({})
  const [loading, setLoading] = useState(true)
  const [error, setError] = useState('')

  useEffect(() => {
    async function loadWarehouses() {
      try {
        const token = localStorage.getItem('token')

        const warehouseResponse = await fetch(
          'http://localhost:8080/api/warehouses',
          {
            headers: {
              Authorization: `Bearer ${token}`,
            },
          },
        )

        if (!warehouseResponse.ok) {
          throw new Error('Failed to load warehouses')
        }

        const warehouseData = await warehouseResponse.json()
        setWarehouses(warehouseData)

        const locationResults = await Promise.all(
          warehouseData.map(async (warehouse: Warehouse) => {
            const response = await fetch(
              `http://localhost:8080/api/locations/warehouse/${warehouse.id}`,
              {
                headers: {
                  Authorization: `Bearer ${token}`,
                },
              },
            )

            if (!response.ok) {
              throw new Error(
                `Failed to load locations for ${warehouse.name}`,
              )
            }

            const data = await response.json()

            return {
              warehouseId: warehouse.id,
              locations: data,
            }
          }),
        )

        const locationMap: Record<number, Location[]> = {}

        locationResults.forEach((result) => {
          locationMap[result.warehouseId] = result.locations
        })

        setLocations(locationMap)
      } catch (error) {
        setError(
          error instanceof Error
            ? error.message
            : 'Unable to load warehouses',
        )
      } finally {
        setLoading(false)
      }
    }

    loadWarehouses()
  }, [])

  return (
    <div>
      <div className="mb-8">
        <p className="mb-2 text-sm font-medium text-blue-600">
          Inventory
        </p>

        <h2 className="text-3xl font-bold tracking-tight text-slate-900">
          Warehouses
        </h2>

        <p className="mt-2 text-slate-600">
          Manage warehouses and their storage locations.
        </p>
      </div>

      {loading && (
        <div className="rounded-xl border border-slate-200 bg-white p-6 text-sm text-slate-500 shadow-sm">
          Loading warehouses...
        </div>
      )}

      {error && (
        <div className="rounded-xl border border-red-200 bg-red-50 p-6 text-sm text-red-600">
          {error}
        </div>
      )}

      {!loading && !error && (
        <div className="space-y-6">
          {warehouses.length === 0 ? (
            <div className="rounded-xl border border-slate-200 bg-white p-12 text-center shadow-sm">
              <p className="font-medium text-slate-700">
                No warehouses found
              </p>

              <p className="mt-1 text-sm text-slate-500">
                Add a warehouse to start organizing your inventory.
              </p>
            </div>
          ) : (
            warehouses.map((warehouse) => (
              <div
                key={warehouse.id}
                className="overflow-hidden rounded-xl border border-slate-200 bg-white shadow-sm"
              >
                <div className="border-b border-slate-200 px-6 py-5">
                  <div className="flex items-start justify-between">
                    <div>
                      <h3 className="text-xl font-semibold text-slate-900">
                        {warehouse.name}
                      </h3>

                      <div className="mt-2 flex flex-wrap gap-4 text-sm text-slate-500">
                        <span>Code: {warehouse.code}</span>
                        <span>Address: {warehouse.address}</span>
                      </div>
                    </div>

                    <span
                      className={`inline-flex rounded-full px-3 py-1 text-xs font-semibold ${
                        warehouse.active
                          ? 'bg-emerald-50 text-emerald-700'
                          : 'bg-slate-100 text-slate-500'
                      }`}
                    >
                      {warehouse.active ? 'Active' : 'Inactive'}
                    </span>
                  </div>
                </div>

                <div className="p-6">
                  <div className="mb-4 flex items-center justify-between">
                    <div>
                      <h4 className="font-semibold text-slate-900">
                        Storage Locations
                      </h4>

                      <p className="mt-1 text-sm text-slate-500">
                        {locations[warehouse.id]?.length ?? 0} location
                        {(locations[warehouse.id]?.length ?? 0) !== 1
                          ? 's'
                          : ''}
                      </p>
                    </div>
                  </div>

                  {locations[warehouse.id]?.length === 0 ? (
                    <div className="rounded-lg border border-dashed border-slate-300 px-6 py-8 text-center">
                      <p className="text-sm font-medium text-slate-700">
                        No locations found
                      </p>

                      <p className="mt-1 text-xs text-slate-500">
                        Add a storage location to this warehouse.
                      </p>
                    </div>
                  ) : (
                    <div className="grid gap-4 md:grid-cols-2">
                      {locations[warehouse.id]?.map((location) => (
                        <div
                          key={location.id}
                          className="rounded-lg border border-slate-200 p-5"
                        >
                          <div className="flex items-start justify-between">
                            <div>
                              <h5 className="font-semibold text-slate-900">
                                {location.name}
                              </h5>

                              <p className="mt-1 font-mono text-xs text-slate-500">
                                {location.code}
                              </p>
                            </div>

                            <span
                              className={`text-xs font-semibold ${
                                location.active
                                  ? 'text-emerald-600'
                                  : 'text-slate-400'
                              }`}
                            >
                              {location.active ? 'Active' : 'Inactive'}
                            </span>
                          </div>

                          {location.description && (
                            <p className="mt-3 text-sm text-slate-600">
                              {location.description}
                            </p>
                          )}
                        </div>
                      ))}
                    </div>
                  )}
                </div>
              </div>
            ))
          )}
        </div>
      )}
    </div>
  )
}

export default Warehouses