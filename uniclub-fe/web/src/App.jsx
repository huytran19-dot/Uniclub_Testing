import { Routes, Route } from "react-router-dom"
import { ThemeProvider } from "./components/theme-provider"
import { Toaster, toast } from "sonner";
import HomePage from "./pages/HomePage"
import ProductsPage from "./pages/Products"
import ProductDetailPage from "./pages/ProductDetailPage"
import CartPage from "./pages/CartPage"
import CheckoutPage from "./pages/CheckoutPage"
import PaymentReturnPage from "./pages/PaymentReturnPage"
import OrdersPage from "./pages/OrdersPage"
import AllOrdersPage from "./pages/AllOrdersPage"
import OrderDetailPage from "./pages/OrderDetailPage"
import LoginPage from "./pages/LoginPage"
import RegisterPage from "./pages/RegisterPage"
import ProfilePage from "./pages/ProfilePage"
import VerifyEmailPage from "./pages/VerifyEmailPage"

function App() {
  return (
    <ThemeProvider defaultTheme="light" storageKey="uniclub-theme">
      <Routes>
        <Route path="/" element={<HomePage />} />
        <Route path="/products" element={<ProductsPage />} />
        <Route path="/products/:id" element={<ProductDetailPage />} />
        <Route path="/cart" element={<CartPage />} />
        <Route path="/checkout" element={<CheckoutPage />} />
        <Route path="/payment/vnpay-return" element={<PaymentReturnPage />} />
        <Route path="/orders" element={<OrdersPage />} />
        <Route path="/admin/orders" element={<AllOrdersPage />} />
        <Route path="/orders/:id" element={<OrderDetailPage />} />
        <Route path="/login" element={<LoginPage />} />
        <Route path="/register" element={<RegisterPage />} />
        <Route path="/verify-email" element={<VerifyEmailPage />} />
        <Route path="/profile" element={<ProfilePage />} />
      </Routes>
      <Toaster />
    </ThemeProvider>
  )
}

export default App
