import { NavLink, Outlet } from 'react-router-dom'

function MainLayout() {
  const navigation = [
    {
      label: 'Dashboard',
      path: '/',
    },
    {
      label: 'Products',
      path: '/products',
    },
    {
      label: 'Warehouses',
      path: '/warehouses',
    },
    {
      label: 'Receipts',
      path: '/receipts',
    },
    {
      label: 'Deliveries',
      path: '/deliveries',
    },
    {
      label: 'Transfers',
      path: '/transfers',
    },
    {
      label: 'Adjustments',
      path: '/adjustments',
    },
    {
      label: 'Stock Movement',
      path: '/stock-movements',
    },
  ]

  return (
    <div className="flex min-h-screen bg-slate-50 text-slate-900">
      <aside className="flex w-64 flex-col border-r border-slate-200 bg-white">
        <div className="border-b border-slate-200 px-6 py-5">
          <h1 className="text-xl font-bold tracking-tight text-slate-900">
            StockSence
          </h1>

          <p className="mt-1 text-xs text-slate-500">
            Inventory Management System
          </p>
        </div>

        <nav className="flex-1 space-y-1 p-4">
          {navigation.map((item) => (
            <NavLink
              key={item.path}
              to={item.path}
              end={item.path === '/'}
              className={({ isActive }) =>
                `block rounded-lg px-4 py-3 text-sm font-medium transition ${
                  isActive
                    ? 'bg-slate-900 text-white'
                    : 'text-slate-600 hover:bg-slate-100 hover:text-slate-900'
                }`
              }
            >
              {item.label}
            </NavLink>
          ))}
        </nav>

        <div className="border-t border-slate-200 p-4">
          <div className="rounded-lg bg-slate-50 px-4 py-3">
            <p className="text-sm font-semibold text-slate-800">
              Inventory Manager
            </p>

            <p className="mt-1 text-xs text-slate-500">
              StockSence Admin
            </p>
          </div>
        </div>
      </aside>

      <div className="flex min-w-0 flex-1 flex-col">
        <header className="flex h-16 items-center justify-between border-b border-slate-200 bg-white px-6">
          <div>
            <p className="text-sm font-medium text-slate-500">
              Inventory Management
            </p>
          </div>

          <div className="flex h-9 w-9 items-center justify-center rounded-full bg-slate-900 text-sm font-semibold text-white">
            IM
          </div>
        </header>

        <main className="flex-1 p-6">
          <Outlet />
        </main>
      </div>
    </div>
  )
}

export default MainLayout