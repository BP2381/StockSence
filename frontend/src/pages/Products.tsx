import { useEffect, useState } from 'react'
import { useNavigate } from 'react-router-dom'

interface Category {
  id: number
  name: string
}

interface Product {
  id: number
  name: string
  sku: string
  category: Category
  unitOfMeasure: string
  active: boolean
}

function Products() {
  const navigate = useNavigate()

  const [products, setProducts] = useState<Product[]>([])
  const [loading, setLoading] = useState(true)
  const [error, setError] = useState('')

  useEffect(() => {
    async function loadProducts() {
      try {
        const token = localStorage.getItem('token')

        const response = await fetch(
          'http://localhost:8080/api/products',
          {
            headers: {
              Authorization: `Bearer ${token}`,
            },
          },
        )

        if (!response.ok) {
          throw new Error('Failed to load products')
        }

        const data = await response.json()
        setProducts(data)
      } catch (error) {
        setError(
          error instanceof Error
            ? error.message
            : 'Unable to load products',
        )
      } finally {
        setLoading(false)
      }
    }

    loadProducts()
  }, [])

  return (
    <div>
      <div className="mb-8 flex items-start justify-between">
        <div>
          <p className="mb-2 text-sm font-medium text-blue-600">
            Inventory
          </p>

          <h2 className="text-3xl font-bold tracking-tight text-slate-900">
            Products
          </h2>

          <p className="mt-2 text-slate-600">
            Manage your products, SKUs, categories, and units of measure.
          </p>
        </div>

        <button
          type="button"
          onClick={() => navigate('/products/add')}
          className="rounded-lg bg-slate-900 px-5 py-3 text-sm font-semibold text-white transition hover:bg-slate-800"
        >
          + Add Product
        </button>
      </div>

      {loading && (
        <div className="rounded-xl border border-slate-200 bg-white p-6 text-sm text-slate-500 shadow-sm">
          Loading products...
        </div>
      )}

      {error && (
        <div className="rounded-xl border border-red-200 bg-red-50 p-6 text-sm text-red-600">
          {error}
        </div>
      )}

      {!loading && !error && (
        <div className="overflow-hidden rounded-xl border border-slate-200 bg-white shadow-sm">
          <div className="border-b border-slate-200 px-6 py-4">
            <h3 className="font-semibold text-slate-900">
              Product List
            </h3>

            <p className="mt-1 text-sm text-slate-500">
              {products.length} product
              {products.length !== 1 ? 's' : ''} found
            </p>
          </div>

          {products.length === 0 ? (
            <div className="px-6 py-12 text-center">
              <p className="font-medium text-slate-700">
                No products found
              </p>

              <p className="mt-1 text-sm text-slate-500">
                Add your first product to start managing inventory.
              </p>
            </div>
          ) : (
            <div className="overflow-x-auto">
              <table className="w-full text-left text-sm">
                <thead className="border-b border-slate-200 bg-slate-50">
                  <tr>
                    <th className="px-6 py-4 font-semibold text-slate-600">
                      Product
                    </th>

                    <th className="px-6 py-4 font-semibold text-slate-600">
                      SKU
                    </th>

                    <th className="px-6 py-4 font-semibold text-slate-600">
                      Category
                    </th>

                    <th className="px-6 py-4 font-semibold text-slate-600">
                      Unit
                    </th>

                    <th className="px-6 py-4 font-semibold text-slate-600">
                      Status
                    </th>
                  </tr>
                </thead>

                <tbody className="divide-y divide-slate-100">
                  {products.map((product) => (
                    <tr
                      key={product.id}
                      className="transition hover:bg-slate-50"
                    >
                      <td className="px-6 py-4">
                        <p className="font-semibold text-slate-900">
                          {product.name}
                        </p>
                      </td>

                      <td className="px-6 py-4 font-mono text-sm text-slate-600">
                        {product.sku}
                      </td>

                      <td className="px-6 py-4 text-slate-600">
                        {product.category?.name ?? '—'}
                      </td>

                      <td className="px-6 py-4 text-slate-600">
                        {product.unitOfMeasure}
                      </td>

                      <td className="px-6 py-4">
                        <span
                          className={`inline-flex rounded-full px-3 py-1 text-xs font-semibold ${
                            product.active
                              ? 'bg-emerald-50 text-emerald-700'
                              : 'bg-slate-100 text-slate-500'
                          }`}
                        >
                          {product.active ? 'Active' : 'Inactive'}
                        </span>
                      </td>
                    </tr>
                  ))}
                </tbody>
              </table>
            </div>
          )}
        </div>
      )}
    </div>
  )
}

export default Products