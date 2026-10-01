import { useEffect, useState } from 'react'
import { useNavigate } from 'react-router-dom'

interface Receipt {
  id: number
  receiptNumber: string
  supplier: string
  status: 'DRAFT' | 'READY' | 'DONE' | 'CANCELED'
  notes: string
  createdAt: string
}

function Receipts() {
  const navigate = useNavigate()

  const [receipts, setReceipts] = useState<Receipt[]>([])
  const [loading, setLoading] = useState(true)
  const [error, setError] = useState('')
  const [validatingId, setValidatingId] = useState<number | null>(null)

  async function loadReceipts() {
    try {
      setError('')

      const token = localStorage.getItem('token')

      const response = await fetch(
        'http://localhost:8080/api/receipts',
        {
          headers: {
            Authorization: `Bearer ${token}`,
          },
        },
      )

      if (!response.ok) {
        throw new Error('Failed to load receipts')
      }

      const data = await response.json()
      setReceipts(data)
    } catch (error) {
      setError(
        error instanceof Error
          ? error.message
          : 'Unable to load receipts',
      )
    } finally {
      setLoading(false)
    }
  }

  useEffect(() => {
    loadReceipts()
  }, [])

  async function handleValidate(receiptId: number) {
    try {
      setError('')
      setValidatingId(receiptId)

      const token = localStorage.getItem('token')

      const response = await fetch(
        `http://localhost:8080/api/receipts/${receiptId}/validate`,
        {
          method: 'PUT',
          headers: {
            Authorization: `Bearer ${token}`,
          },
        },
      )

      const data = await response.json()

      if (!response.ok) {
        throw new Error(data.message || 'Failed to validate receipt')
      }

      await loadReceipts()
    } catch (error) {
      setError(
        error instanceof Error
          ? error.message
          : 'Unable to validate receipt',
      )
    } finally {
      setValidatingId(null)
    }
  }

  function getStatusClass(status: Receipt['status']) {
    switch (status) {
      case 'DONE':
        return 'bg-emerald-50 text-emerald-700'

      case 'CANCELED':
        return 'bg-red-50 text-red-700'

      case 'READY':
        return 'bg-blue-50 text-blue-700'

      default:
        return 'bg-amber-50 text-amber-700'
    }
  }

  return (
    <div>
      <div className="mb-8 flex items-start justify-between">
        <div>
          <p className="mb-2 text-sm font-medium text-blue-600">
            Operations
          </p>

          <h2 className="text-3xl font-bold tracking-tight text-slate-900">
            Receipts
          </h2>

          <p className="mt-2 text-slate-600">
            Receive products from suppliers and increase inventory stock.
          </p>
        </div>

        <button
          type="button"
          onClick={() => navigate('/receipts/add')}
          className="rounded-lg bg-slate-900 px-5 py-3 text-sm font-semibold text-white transition hover:bg-slate-800"
        >
          + New Receipt
        </button>
      </div>

      {error && (
        <div className="mb-6 rounded-lg border border-red-200 bg-red-50 px-4 py-3 text-sm text-red-600">
          {error}
        </div>
      )}

      {loading ? (
        <div className="rounded-xl border border-slate-200 bg-white p-6 text-sm text-slate-500 shadow-sm">
          Loading receipts...
        </div>
      ) : (
        <div className="overflow-hidden rounded-xl border border-slate-200 bg-white shadow-sm">
          <div className="border-b border-slate-200 px-6 py-4">
            <h3 className="font-semibold text-slate-900">
              Receipt List
            </h3>

            <p className="mt-1 text-sm text-slate-500">
              {receipts.length} receipt
              {receipts.length !== 1 ? 's' : ''} found
            </p>
          </div>

          {receipts.length === 0 ? (
            <div className="px-6 py-12 text-center">
              <p className="font-medium text-slate-700">
                No receipts found
              </p>

              <p className="mt-1 text-sm text-slate-500">
                Create a receipt when products arrive at your warehouse.
              </p>
            </div>
          ) : (
            <div className="overflow-x-auto">
              <table className="w-full text-left text-sm">
                <thead className="border-b border-slate-200 bg-slate-50">
                  <tr>
                    <th className="px-6 py-4 font-semibold text-slate-600">
                      Receipt
                    </th>

                    <th className="px-6 py-4 font-semibold text-slate-600">
                      Supplier
                    </th>

                    <th className="px-6 py-4 font-semibold text-slate-600">
                      Status
                    </th>

                    <th className="px-6 py-4 font-semibold text-slate-600">
                      Created
                    </th>

                    <th className="px-6 py-4 text-right font-semibold text-slate-600">
                      Action
                    </th>
                  </tr>
                </thead>

                <tbody className="divide-y divide-slate-100">
                  {receipts.map((receipt) => (
                    <tr
                      key={receipt.id}
                      className="transition hover:bg-slate-50"
                    >
                      <td className="px-6 py-4">
                        <p className="font-semibold text-slate-900">
                          {receipt.receiptNumber}
                        </p>
                      </td>

                      <td className="px-6 py-4 text-slate-600">
                        {receipt.supplier || '—'}
                      </td>

                      <td className="px-6 py-4">
                        <span
                          className={`inline-flex rounded-full px-3 py-1 text-xs font-semibold ${getStatusClass(
                            receipt.status,
                          )}`}
                        >
                          {receipt.status}
                        </span>
                      </td>

                      <td className="px-6 py-4 text-slate-500">
                        {new Date(receipt.createdAt).toLocaleDateString()}
                      </td>

                      <td className="px-6 py-4 text-right">
                        {receipt.status !== 'DONE' &&
                          receipt.status !== 'CANCELED' && (
                            <button
                              type="button"
                              onClick={() => handleValidate(receipt.id)}
                              disabled={validatingId === receipt.id}
                              className="rounded-lg bg-emerald-600 px-4 py-2 text-xs font-semibold text-white transition hover:bg-emerald-700 disabled:cursor-not-allowed disabled:opacity-60"
                            >
                              {validatingId === receipt.id
                                ? 'Validating...'
                                : 'Validate'}
                            </button>
                          )}

                        {receipt.status === 'DONE' && (
                          <span className="text-xs font-medium text-emerald-600">
                            Stock Updated
                          </span>
                        )}
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

export default Receipts