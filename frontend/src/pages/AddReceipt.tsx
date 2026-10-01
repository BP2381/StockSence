import { useEffect, useState } from 'react'
import { useNavigate } from 'react-router-dom'

interface Product {
  id: number
  name: string
  sku: string
  unitOfMeasure: string
}

interface Location {
  id: number
  name: string
  code: string
}

interface ReceiptItemForm {
  productId: string
  locationId: string
  quantity: string
}

function AddReceipt() {
  const navigate = useNavigate()

  const [receiptNumber, setReceiptNumber] = useState('')
  const [supplier, setSupplier] = useState('')
  const [notes, setNotes] = useState('')

  const [products, setProducts] = useState<Product[]>([])
  const [locations, setLocations] = useState<Location[]>([])

  const [item, setItem] = useState<ReceiptItemForm>({
    productId: '',
    locationId: '',
    quantity: '',
  })

  const [loading, setLoading] = useState(true)
  const [saving, setSaving] = useState(false)
  const [error, setError] = useState('')
  const [success, setSuccess] = useState('')

  useEffect(() => {
    async function loadData() {
      try {
        const token = localStorage.getItem('token')

        const [productsResponse, locationsResponse] = await Promise.all([
          fetch('http://localhost:8080/api/products', {
            headers: {
              Authorization: `Bearer ${token}`,
            },
          }),
          fetch('http://localhost:8080/api/locations/warehouse/1', {
            headers: {
              Authorization: `Bearer ${token}`,
            },
          }),
        ])

        if (!productsResponse.ok) {
          throw new Error('Failed to load products')
        }

        if (!locationsResponse.ok) {
          throw new Error('Failed to load locations')
        }

        const productsData = await productsResponse.json()
        const locationsData = await locationsResponse.json()

        setProducts(productsData)
        setLocations(locationsData)
      } catch (error) {
        setError(
          error instanceof Error
            ? error.message
            : 'Unable to load receipt data',
        )
      } finally {
        setLoading(false)
      }
    }

    loadData()
  }, [])

  async function handleSubmit(event: React.FormEvent<HTMLFormElement>) {
    event.preventDefault()

    setError('')
    setSuccess('')

    if (!receiptNumber.trim()) {
      setError('Receipt number is required')
      return
    }

    if (!item.productId) {
      setError('Please select a product')
      return
    }

    if (!item.locationId) {
      setError('Please select a location')
      return
    }

    if (!item.quantity || Number(item.quantity) <= 0) {
      setError('Quantity must be greater than 0')
      return
    }

    try {
      setSaving(true)

      const token = localStorage.getItem('token')

      const receiptResponse = await fetch(
        'http://localhost:8080/api/receipts',
        {
          method: 'POST',
          headers: {
            Authorization: `Bearer ${token}`,
            'Content-Type': 'application/json',
          },
          body: JSON.stringify({
            receiptNumber: receiptNumber.trim(),
            supplier: supplier.trim(),
            notes: notes.trim(),
          }),
        },
      )

      const receiptData = await receiptResponse.json()

      if (!receiptResponse.ok) {
        throw new Error(
          receiptData.message || 'Failed to create receipt',
        )
      }

      const itemResponse = await fetch(
        'http://localhost:8080/api/receipt-items',
        {
          method: 'POST',
          headers: {
            Authorization: `Bearer ${token}`,
            'Content-Type': 'application/json',
          },
          body: JSON.stringify({
            receipt: {
              id: receiptData.id,
            },
            product: {
              id: Number(item.productId),
            },
            location: {
              id: Number(item.locationId),
            },
            quantity: Number(item.quantity),
          }),
        },
      )

      const itemData = await itemResponse.json()

      if (!itemResponse.ok) {
        throw new Error(
          itemData.message || 'Failed to add receipt item',
        )
      }

      setSuccess(
        `Receipt ${receiptData.receiptNumber} created successfully.`,
      )

      setReceiptNumber('')
      setSupplier('')
      setNotes('')
      setItem({
        productId: '',
        locationId: '',
        quantity: '',
      })
    } catch (error) {
      setError(
        error instanceof Error
          ? error.message
          : 'Unable to create receipt',
      )
    } finally {
      setSaving(false)
    }
  }

  if (loading) {
    return (
      <div>
        <p className="text-sm text-slate-500">Loading receipt form...</p>
      </div>
    )
  }

  return (
    <div>
      <div className="mb-8">
        <button
          type="button"
          onClick={() => navigate('/receipts')}
          className="mb-4 text-sm font-medium text-blue-600 hover:text-blue-700"
        >
          ← Back to Receipts
        </button>

        <p className="mb-2 text-sm font-medium text-blue-600">
          Operations
        </p>

        <h2 className="text-3xl font-bold tracking-tight text-slate-900">
          New Receipt
        </h2>

        <p className="mt-2 text-slate-600">
          Record products received from a supplier.
        </p>
      </div>

      {error && (
        <div className="mb-6 rounded-lg border border-red-200 bg-red-50 px-4 py-3 text-sm text-red-600">
          {error}
        </div>
      )}

      {success && (
        <div className="mb-6 rounded-lg border border-emerald-200 bg-emerald-50 px-4 py-3 text-sm text-emerald-700">
          {success}
        </div>
      )}

      <form
        onSubmit={handleSubmit}
        className="max-w-3xl rounded-xl border border-slate-200 bg-white p-6 shadow-sm"
      >
        <div className="grid gap-6 md:grid-cols-2">
          <div>
            <label className="mb-2 block text-sm font-semibold text-slate-700">
              Receipt Number
            </label>

            <input
              type="text"
              value={receiptNumber}
              onChange={(event) => setReceiptNumber(event.target.value)}
              placeholder="REC-002"
              className="w-full rounded-lg border border-slate-300 px-4 py-3 text-sm outline-none transition focus:border-blue-500 focus:ring-2 focus:ring-blue-100"
            />
          </div>

          <div>
            <label className="mb-2 block text-sm font-semibold text-slate-700">
              Supplier
            </label>

            <input
              type="text"
              value={supplier}
              onChange={(event) => setSupplier(event.target.value)}
              placeholder="ABC Steel Suppliers"
              className="w-full rounded-lg border border-slate-300 px-4 py-3 text-sm outline-none transition focus:border-blue-500 focus:ring-2 focus:ring-blue-100"
            />
          </div>
        </div>

        <div className="mt-6">
          <label className="mb-2 block text-sm font-semibold text-slate-700">
            Notes
          </label>

          <textarea
            value={notes}
            onChange={(event) => setNotes(event.target.value)}
            placeholder="Optional notes about this receipt"
            rows={3}
            className="w-full rounded-lg border border-slate-300 px-4 py-3 text-sm outline-none transition focus:border-blue-500 focus:ring-2 focus:ring-blue-100"
          />
        </div>

        <div className="my-8 border-t border-slate-200" />

        <div>
          <h3 className="text-lg font-semibold text-slate-900">
            Receipt Item
          </h3>

          <p className="mt-1 text-sm text-slate-500">
            Select the product, destination location, and quantity received.
          </p>
        </div>

        <div className="mt-6 grid gap-6 md:grid-cols-3">
          <div>
            <label className="mb-2 block text-sm font-semibold text-slate-700">
              Product
            </label>

            <select
              value={item.productId}
              onChange={(event) =>
                setItem({
                  ...item,
                  productId: event.target.value,
                })
              }
              className="w-full rounded-lg border border-slate-300 bg-white px-4 py-3 text-sm outline-none transition focus:border-blue-500 focus:ring-2 focus:ring-blue-100"
            >
              <option value="">Select product</option>

              {products.map((product) => (
                <option key={product.id} value={product.id}>
                  {product.name} ({product.sku})
                </option>
              ))}
            </select>
          </div>

          <div>
            <label className="mb-2 block text-sm font-semibold text-slate-700">
              Location
            </label>

            <select
              value={item.locationId}
              onChange={(event) =>
                setItem({
                  ...item,
                  locationId: event.target.value,
                })
              }
              className="w-full rounded-lg border border-slate-300 bg-white px-4 py-3 text-sm outline-none transition focus:border-blue-500 focus:ring-2 focus:ring-blue-100"
            >
              <option value="">Select location</option>

              {locations.map((location) => (
                <option key={location.id} value={location.id}>
                  {location.name} ({location.code})
                </option>
              ))}
            </select>
          </div>

          <div>
            <label className="mb-2 block text-sm font-semibold text-slate-700">
              Quantity
            </label>

            <input
              type="number"
              min="0.01"
              step="0.01"
              value={item.quantity}
              onChange={(event) =>
                setItem({
                  ...item,
                  quantity: event.target.value,
                })
              }
              placeholder="100"
              className="w-full rounded-lg border border-slate-300 px-4 py-3 text-sm outline-none transition focus:border-blue-500 focus:ring-2 focus:ring-blue-100"
            />
          </div>
        </div>

        <div className="mt-8 flex justify-end gap-3">
          <button
            type="button"
            onClick={() => navigate('/receipts')}
            className="rounded-lg border border-slate-300 px-5 py-3 text-sm font-semibold text-slate-700 transition hover:bg-slate-50"
          >
            Cancel
          </button>

          <button
            type="submit"
            disabled={saving}
            className="rounded-lg bg-slate-900 px-5 py-3 text-sm font-semibold text-white transition hover:bg-slate-800 disabled:cursor-not-allowed disabled:opacity-60"
          >
            {saving ? 'Creating...' : 'Create Receipt'}
          </button>
        </div>
      </form>
    </div>
  )
}

export default AddReceipt