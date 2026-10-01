import { Navigate, Route, Routes } from 'react-router-dom'
import Login from './pages/Login'
import Dashboard from './pages/Dashboard'
import Products from './pages/Products'
import AddProduct from './pages/AddProduct'
import Warehouses from './pages/Warehouses'
import Receipts from './pages/Receipts'
import AddReceipt from './pages/AddReceipt'
import MainLayout from './layouts/MainLayout'

function PlaceholderPage({ title }: { title: string }) {
  return (
    <div>
      <p className="mb-2 text-sm font-medium text-blue-600">
        StockSence
      </p>

      <h2 className="text-3xl font-bold tracking-tight text-slate-900">
        {title}
      </h2>

      <p className="mt-2 text-slate-600">
        This module will be built next.
      </p>
    </div>
  )
}

function App() {
  const isAuthenticated = Boolean(localStorage.getItem('token'))

  if (!isAuthenticated) {
    return <Login onLogin={() => window.location.reload()} />
  }

  return (
    <Routes>
      <Route element={<MainLayout />}>
        <Route path="/" element={<Dashboard />} />

        <Route path="/products" element={<Products />} />

        <Route path="/products/add" element={<AddProduct />} />

        <Route path="/warehouses" element={<Warehouses />} />

        <Route path="/receipts" element={<Receipts />} />

        <Route path="/receipts/add" element={<AddReceipt />} />

        <Route
          path="/deliveries"
          element={<PlaceholderPage title="Deliveries" />}
        />

        <Route
          path="/transfers"
          element={<PlaceholderPage title="Internal Transfers" />}
        />

        <Route
          path="/adjustments"
          element={<PlaceholderPage title="Inventory Adjustments" />}
        />

        <Route
          path="/stock-movements"
          element={<PlaceholderPage title="Stock Movement" />}
        />

        <Route
          path="*"
          element={<Navigate to="/" replace />}
        />
      </Route>
    </Routes>
  )
}

export default App