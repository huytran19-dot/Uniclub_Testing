import React from "react"
import { Routes, Route, Navigate } from "react-router-dom"
import { AuthProvider, useAuth } from "./lib/auth.jsx"
import Layout from "./components/Layout"
import ProtectedRoute from "./components/ProtectedRoute"
import Login from "./pages/Login"
import Dashboard from "./pages/Dashboard"
import CategoryList from "./pages/categories/List"
import CategoryForm from "./pages/categories/Form"
import BrandList from "./pages/brands/List"
import BrandForm from "./pages/brands/Form"
import SizeList from "./pages/sizes/List"
import SizeForm from "./pages/sizes/Form"
import ColorList from "./pages/colors/List"
import ColorForm from "./pages/colors/Form"
import ProductList from "./pages/products/List"
import ProductForm from "./pages/products/Form"
import ProductVariants from "./pages/products/Variants"
import VariantList from "./pages/variants/List"
import VariantForm from "./pages/variants/Form"
import OrderList from "./pages/orders/List"
import OrderDetail from "./pages/orders/Detail"
import SupplierList from "./pages/suppliers/List"
import SupplierForm from "./pages/suppliers/Form"
import GrnList from "./pages/grn/List"
import GrnNew from "./pages/grn/New"
import GrnDetail from "./pages/grn/Detail"

function AppRoutes() {
  const { isAuthenticated } = useAuth()

  return (
    <Routes>
      <Route path="/login" element={isAuthenticated() ? <Navigate to="/dashboard" replace /> : <Login />} />
      <Route path="/" element={<ProtectedRoute><Layout><Navigate to="/dashboard" replace /></Layout></ProtectedRoute>} />
      <Route path="/dashboard" element={<ProtectedRoute><Layout><Dashboard /></Layout></ProtectedRoute>} />
      <Route path="/categories" element={<ProtectedRoute><Layout><CategoryList /></Layout></ProtectedRoute>} />
      <Route path="/categories/new" element={<ProtectedRoute><Layout><CategoryForm /></Layout></ProtectedRoute>} />
      <Route path="/categories/:id" element={<ProtectedRoute><Layout><CategoryForm /></Layout></ProtectedRoute>} />
      <Route path="/brands" element={<ProtectedRoute><Layout><BrandList /></Layout></ProtectedRoute>} />
      <Route path="/brands/new" element={<ProtectedRoute><Layout><BrandForm /></Layout></ProtectedRoute>} />
      <Route path="/brands/:id" element={<ProtectedRoute><Layout><BrandForm /></Layout></ProtectedRoute>} />
      <Route path="/sizes" element={<ProtectedRoute><Layout><SizeList /></Layout></ProtectedRoute>} />
      <Route path="/sizes/new" element={<ProtectedRoute><Layout><SizeForm /></Layout></ProtectedRoute>} />
      <Route path="/sizes/:id" element={<ProtectedRoute><Layout><SizeForm /></Layout></ProtectedRoute>} />
      <Route path="/colors" element={<ProtectedRoute><Layout><ColorList /></Layout></ProtectedRoute>} />
      <Route path="/colors/new" element={<ProtectedRoute><Layout><ColorForm /></Layout></ProtectedRoute>} />
      <Route path="/colors/:id" element={<ProtectedRoute><Layout><ColorForm /></Layout></ProtectedRoute>} />
      <Route path="/products" element={<ProtectedRoute><Layout><ProductList /></Layout></ProtectedRoute>} />
      <Route path="/products/new" element={<ProtectedRoute><Layout><ProductForm /></Layout></ProtectedRoute>} />
      <Route path="/products/:id" element={<ProtectedRoute><Layout><ProductForm /></Layout></ProtectedRoute>} />
      <Route path="/products/:productId/variants" element={<ProtectedRoute><Layout><ProductVariants /></Layout></ProtectedRoute>} />
      <Route path="/variants" element={<ProtectedRoute><Layout><VariantList /></Layout></ProtectedRoute>} />
      <Route path="/variants/new" element={<ProtectedRoute><Layout><VariantForm /></Layout></ProtectedRoute>} />
      <Route path="/variants/:sku" element={<ProtectedRoute><Layout><VariantForm /></Layout></ProtectedRoute>} />
      <Route path="/orders" element={<ProtectedRoute><Layout><OrderList /></Layout></ProtectedRoute>} />
      <Route path="/orders/:id" element={<ProtectedRoute><Layout><OrderDetail /></Layout></ProtectedRoute>} />
      <Route path="/suppliers" element={<ProtectedRoute><Layout><SupplierList /></Layout></ProtectedRoute>} />
      <Route path="/suppliers/new" element={<ProtectedRoute><Layout><SupplierForm /></Layout></ProtectedRoute>} />
      <Route path="/suppliers/:id" element={<ProtectedRoute><Layout><SupplierForm /></Layout></ProtectedRoute>} />
      <Route path="/grn" element={<ProtectedRoute><Layout><GrnList /></Layout></ProtectedRoute>} />
      <Route path="/grn/new" element={<ProtectedRoute><Layout><GrnNew /></Layout></ProtectedRoute>} />
      <Route path="/grn/:id" element={<ProtectedRoute><Layout><GrnDetail /></Layout></ProtectedRoute>} />
      <Route path="/grn/:id/details" element={<ProtectedRoute><Layout><GrnDetail /></Layout></ProtectedRoute>} />
    </Routes>
  )
}

export default function App() {
  return (
    <AuthProvider>
      <AppRoutes />
    </AuthProvider>
  )
}
