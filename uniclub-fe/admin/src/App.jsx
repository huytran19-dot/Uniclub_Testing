import React from "react"
import { Routes, Route } from "react-router-dom"
import Layout from "./components/Layout"
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
import VariantList from "./pages/variants/List"
import VariantForm from "./pages/variants/Form"
import OrderList from "./pages/orders/List"
import OrderDetail from "./pages/orders/Detail"
import SupplierList from "./pages/suppliers/List"
import SupplierForm from "./pages/suppliers/Form"
import GrnList from "./pages/grn/List"
import GrnNew from "./pages/grn/New"
import GrnDetail from "./pages/grn/Detail"
import Settings from "./pages/settings/Settings"

export default function App() {
  return (
    <Layout>
      <Routes>
        <Route path="/dashboard" element={<Dashboard />} />
        <Route path="/categories" element={<CategoryList />} />
        <Route path="/categories/new" element={<CategoryForm />} />
        <Route path="/categories/:id" element={<CategoryForm />} />
        <Route path="/brands" element={<BrandList />} />
        <Route path="/brands/new" element={<BrandForm />} />
        <Route path="/brands/:id" element={<BrandForm />} />
        <Route path="/sizes" element={<SizeList />} />
        <Route path="/sizes/new" element={<SizeForm />} />
        <Route path="/sizes/:id" element={<SizeForm />} />
        <Route path="/colors" element={<ColorList />} />
        <Route path="/colors/new" element={<ColorForm />} />
        <Route path="/colors/:id" element={<ColorForm />} />
        <Route path="/products" element={<ProductList />} />
        <Route path="/products/new" element={<ProductForm />} />
        <Route path="/products/:id" element={<ProductForm />} />
        <Route path="/variants" element={<VariantList />} />
        <Route path="/variants/new" element={<VariantForm />} />
        <Route path="/variants/:sku" element={<VariantForm />} />
        <Route path="/orders" element={<OrderList />} />
        <Route path="/orders/:id" element={<OrderDetail />} />
        <Route path="/suppliers" element={<SupplierList />} />
        <Route path="/suppliers/new" element={<SupplierForm />} />
        <Route path="/suppliers/:id" element={<SupplierForm />} />
        <Route path="/grn" element={<GrnList />} />
        <Route path="/grn/new" element={<GrnNew />} />
        <Route path="/grn/:id" element={<GrnDetail />} />
        <Route path="/settings" element={<Settings />} />
        <Route path="/" element={<Dashboard />} />
      </Routes>
    </Layout>
  )
}
