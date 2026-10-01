import { useEffect, useState } from 'react'
import { getDashboardSummary } from '../services/api'
import type { DashboardSummary } from '../services/dashboard'

function Dashboard() {
  const [dashboard, setDashboard] = useState<DashboardSummary | null>(null)
  const [loading, setLoading] = useState(true)
  const [error, setError] = useState('')

  useEffect(() => {
    async function loadDashboard() {
      try {
        setError('')
        const data = await getDashboardSummary()
        setDashboard(data)
      } catch (error) {
        setError(
          error instanceof Error
            ? error.message
            : 'Unable to load dashboard'
        )
      } finally {
        setLoading(false)
      }
    }

    loadDashboard()
  }, [])

  const cards = [
    {
      label: 'Total Products',
      value: dashboard?.totalProducts ?? 0,
      valueClass: 'text-slate-900',
    },
    {
      label: 'Total Stock',
      value: dashboard?.totalStock ?? 0,
      valueClass: 'text-slate-900',
    },
    {
      label: 'Low Stock',
      value: dashboard?.lowStockProducts ?? 0,
      valueClass: 'text-amber-600',
    },
    {
      label: 'Out of Stock',
      value: dashboard?.outOfStockProducts ?? 0,
      valueClass: 'text-red-600',
    },
    {
      label: 'Pending Receipts',
      value: dashboard?.pendingReceipts ?? 0,
      valueClass: 'text-emerald-600',
    },
    {
      label: 'Pending Deliveries',
      value: dashboard?.pendingDeliveries ?? 0,
      valueClass: 'text-blue-600',
    },
    {
      label: 'Pending Transfers',
      value: dashboard?.pendingTransfers ?? 0,
      valueClass: 'text-violet-600',
    },
  ]

  return (
    <div>
      <div className="mb-8">
        <p className="mb-2 text-sm font-medium text-blue-600">
          Inventory Overview
        </p>

        <h2 className="text-3xl font-bold tracking-tight text-slate-900">
          Welcome to StockSence
        </h2>

        <p className="mt-2 max-w-2xl text-slate-600">
          Manage products, stock movements, warehouses, receipts,
          deliveries, and inventory operations from one centralized system.
        </p>
      </div>

      {loading && (
        <div className="mb-6 rounded-xl border border-slate-200 bg-white p-6 text-sm text-slate-500 shadow-sm">
          Loading dashboard...
        </div>
      )}

      {error && (
        <div className="mb-6 rounded-xl border border-red-200 bg-red-50 p-6 text-sm text-red-600">
          {error}
        </div>
      )}

      <section className="grid gap-5 sm:grid-cols-2 lg:grid-cols-4">
        {cards.map((card) => (
          <div
            key={card.label}
            className="rounded-xl border border-slate-200 bg-white p-6 shadow-sm"
          >
            <p className="text-sm font-medium text-slate-500">
              {card.label}
            </p>

            <p
              className={`mt-3 text-3xl font-bold ${card.valueClass}`}
            >
              {card.value}
            </p>
          </div>
        ))}
      </section>
    </div>
  )
}

export default Dashboard