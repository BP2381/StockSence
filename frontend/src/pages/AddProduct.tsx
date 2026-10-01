import { useEffect, useState } from 'react'
import { useNavigate } from 'react-router-dom'

interface Category {
  id: number
  name: string
}

function AddProduct() {
  const navigate = useNavigate()

  const [categories, setCategories] = useState<Category[]>([])
  const [name, setName] = useState('')
  const [sku, setSku] = useState('')
  const [categoryId, setCategoryId] = useState('')
  const [unitOfMeasure, setUnitOfMeasure] = useState('')
  const [active, setActive] = useState(true)

  const [loadingCategories, setLoadingCategories] = useState(true)
  const [saving, setSaving] = useState(false)
  const [error, setError] = useState('')
  const [success, setSuccess] = useState('')

  useEffect(() => {
    async function loadCategories() {
      try {
        const token = localStorage.getItem('token')

        const response = await fetch(
          'http://localhost:8080/api/categories',
          {
            headers: {
              Authorization: `Bearer ${token}`,
            },
          },
        )

        if (!response.ok) {
          throw new Error('Failed to load categories')
        }

        const data = await response.json()
        setCategories(data)
      } catch (error) {
        setError(
          error instanceof Error
            ? error.message
            : 'Unable to load categories',
        )
      } finally {
        setLoadingCategories(false)
      }
    }

    loadCategories()
  }, [])

  async function handleSubmit(event: React.FormEvent<HTMLFormElement>) {
    event.preventDefault()

    setError('')
    setSuccess('')
    setSaving(true)

    try {
      const token = localStorage.getItem('token')

      const response = await fetch('http://localhost:8080/api/products', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
          Authorization: `Bearer ${token}`,
        },
        body: JSON.stringify({
          name,
          sku,
          category: {
            id: Number(categoryId),
          },
          unitOfMeasure,
          active,
        }),
      })

      const data = await response.json()

      if (!response.ok) {
        throw new Error(data.message || 'Failed to create product')
      }

      setSuccess('Product created successfully.')

      setName('')
      setSku('')
      setCategoryId('')
      setUnitOfMeasure('')
      setActive(true)
    } catch (error) {
      setError(
        error instanceof Error
          ? error.message
          : 'Unable to create product',
      )
    } finally {
      setSaving(false)
    }
  }

  return (
    <div>
      <div className="mb-8">
        <button
          type="button"
          onClick={() => navigate('/products')}
          className="mb-4 text-sm font-medium text-slate-500 transition hover:text-slate-900"
        >
          ← Back to Products
        </button>

        <p className="mb-2 text-sm font-medium text-blue-600">
          Inventory
        </p>

        <h2 className="text-3xl font-bold tracking-tight text-slate-900">
          Add Product
        </h2>

        <p className="mt-2 text-slate-600">
          Create a new product for your inventory.
        </p>
      </div>

      <div className="max-w-3xl rounded-xl border border-slate-200 bg-white p-8 shadow-sm">
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

        <form onSubmit={handleSubmit} className="space-y-6">
          <div className="grid gap-6 md:grid-cols-2">
            <div>
              <label
                htmlFor="name"
                className="mb-2 block text-sm font-medium text-slate-700"
              >
                Product Name
              </label>

              <input
                id="name"
                type="text"
                value={name}
                onChange={(event) => setName(event.target.value)}
                placeholder="e.g. Steel Sheet"
                required
                className="w-full rounded-lg border border-slate-300 px-4 py-3 text-sm outline-none transition focus:border-blue-500 focus:ring-2 focus:ring-blue-100"
              />
            </div>

            <div>
              <label
                htmlFor="sku"
                className="mb-2 block text-sm font-medium text-slate-700"
              >
                SKU
              </label>

              <input
                id="sku"
                type="text"
                value={sku}
                onChange={(event) => setSku(event.target.value)}
                placeholder="e.g. STL-001"
                required
                className="w-full rounded-lg border border-slate-300 px-4 py-3 text-sm outline-none transition focus:border-blue-500 focus:ring-2 focus:ring-blue-100"
              />
            </div>

            <div>
              <label
                htmlFor="category"
                className="mb-2 block text-sm font-medium text-slate-700"
              >
                Category
              </label>

              <select
                id="category"
                value={categoryId}
                onChange={(event) => setCategoryId(event.target.value)}
                required
                disabled={loadingCategories}
                className="w-full rounded-lg border border-slate-300 bg-white px-4 py-3 text-sm outline-none transition focus:border-blue-500 focus:ring-2 focus:ring-blue-100"
              >
                <option value="">
                  {loadingCategories
                    ? 'Loading categories...'
                    : 'Select a category'}
                </option>

                {categories.map((category) => (
                  <option key={category.id} value={category.id}>
                    {category.name}
                  </option>
                ))}
              </select>
            </div>

            <div>
              <label
                htmlFor="unitOfMeasure"
                className="mb-2 block text-sm font-medium text-slate-700"
              >
                Unit of Measure
              </label>

              <input
                id="unitOfMeasure"
                type="text"
                value={unitOfMeasure}
                onChange={(event) => setUnitOfMeasure(event.target.value)}
                placeholder="e.g. kg, pcs, liters"
                required
                className="w-full rounded-lg border border-slate-300 px-4 py-3 text-sm outline-none transition focus:border-blue-500 focus:ring-2 focus:ring-blue-100"
              />
            </div>
          </div>

          <div className="rounded-lg border border-slate-200 bg-slate-50 p-4">
            <label className="flex cursor-pointer items-center gap-3">
              <input
                type="checkbox"
                checked={active}
                onChange={(event) => setActive(event.target.checked)}
                className="h-4 w-4 rounded border-slate-300"
              />

              <div>
                <p className="text-sm font-medium text-slate-800">
                  Active Product
                </p>

                <p className="mt-1 text-xs text-slate-500">
                  Active products can be used in inventory operations.
                </p>
              </div>
            </label>
          </div>

          <div className="flex items-center justify-end gap-3 border-t border-slate-200 pt-6">
            <button
              type="button"
              onClick={() => navigate('/products')}
              className="rounded-lg border border-slate-300 px-5 py-3 text-sm font-semibold text-slate-700 transition hover:bg-slate-50"
            >
              Cancel
            </button>

            <button
              type="submit"
              disabled={saving || loadingCategories}
              className="rounded-lg bg-slate-900 px-5 py-3 text-sm font-semibold text-white transition hover:bg-slate-800 disabled:cursor-not-allowed disabled:opacity-60"
            >
              {saving ? 'Creating...' : 'Create Product'}
            </button>
          </div>
        </form>
      </div>
    </div>
  )
}

export default AddProduct