import { useEffect, useState } from 'react'
import Login from './pages/Login'
import { getDashboardSummary } from './services/api'
import type { DashboardSummary } from './services/dashboard'

function App() {
  const [isAuthenticated, setIsAuthenticated] = useState(
    Boolean(localStorage.getItem('token'))
  )

  const [dashboard, setDashboard] = useState<DashboardSummary | null>(null)
  const [loading, setLoading] = useState(true)
  const [error, setError] = useState('')

  useEffect(() => {
    if (!isAuthenticated) {
      setLoading(false)
      return
    }

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
  }, [isAuthenticated])

  if (!isAuthenticated) {
    return <Login onLogin={() => setIsAuthenticated(true)} />
  }

  return (
    <div className="min-h-screen bg-slate-50 text-slate-900">
      <header className="border-b border-slate-200 bg-white">
        <div className="mx-auto flex h-16 max-w-7xl items-center justify-between px-6">
          <div>
            <h1 className="text-xl font-bold tracking-tight text-slate-900">
              StockSence
            </h1>

            <p className="text-xs text-slate-500">
              Inventory Management System
            </p>
          </div>

          <div className="flex items-center gap-3">
            <span className="hidden text-sm text-slate-600 sm:block">
              Inventory Manager
            </span>

            <div className="flex h-9 w-9 items-center justify-center rounded-full bg-slate-900 text-sm font-semibold text-white">
              IM
            </div>
          </div>
        </div>
      </header>

      <main className="mx-auto max-w-7xl px-6 py-10">
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
          <div className="rounded-xl border border-slate-200 bg-white p-6 shadow-sm">
            <p className="text-sm font-medium text-slate-500">
              Total Products
            </p>

            <p className="mt-3 text-3xl font-bold text-slate-900">
              {dashboard?.totalProducts ?? 0}
            </p>
          </div>

          <div className="rounded-xl border border-slate-200 bg-white p-6 shadow-sm">
            <p className="text-sm font-medium text-slate-500">
              Total Stock
            </p>

            <p className="mt-3 text-3xl font-bold text-slate-900">
              {dashboard?.totalStock ?? 0}
            </p>
          </div>

          <div className="rounded-xl border border-slate-200 bg-white p-6 shadow-sm">
            <p className="text-sm font-medium text-slate-500">
              Low Stock
            </p>

            <p className="mt-3 text-3xl font-bold text-amber-600">
              {dashboard?.lowStockProducts ?? 0}
            </p>
          </div>

          <div className="rounded-xl border border-slate-200 bg-white p-6 shadow-sm">
            <p className="text-sm font-medium text-slate-500">
              Out of Stock
            </p>

            <p className="mt-3 text-3xl font-bold text-red-600">
              {dashboard?.outOfStockProducts ?? 0}
            </p>
          </div>

          <div className="rounded-xl border border-slate-200 bg-white p-6 shadow-sm">
            <p className="text-sm font-medium text-slate-500">
              Pending Receipts
            </p>

            <p className="mt-3 text-3xl font-bold text-emerald-600">
              {dashboard?.pendingReceipts ?? 0}
            </p>
          </div>

          <div className="rounded-xl border border-slate-200 bg-white p-6 shadow-sm">
            <p className="text-sm font-medium text-slate-500">
              Pending Deliveries
            </p>

            <p className="mt-3 text-3xl font-bold text-blue-600">
              {dashboard?.pendingDeliveries ?? 0}
            </p>
          </div>

          <div className="rounded-xl border border-slate-200 bg-white p-6 shadow-sm">
            <p className="text-sm font-medium text-slate-500">
              Pending Transfers
            </p>

            <p className="mt-3 text-3xl font-bold text-violet-600">
              {dashboard?.pendingTransfers ?? 0}
            </p>
          </div>
        </section>
      </main>
    </div>
  )
}

export default App