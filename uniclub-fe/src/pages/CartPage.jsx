import React, { useState, useEffect } from "react"
import { useNavigate, Link } from "react-router-dom"
import { PageLayout } from "../components/PageLayout"
import { CartLineItem } from "@/components/cart/CartLineItem"
import { CartSummary } from "@/components/cart/CartSummary"
import { Button } from "@/components/ui/button"
import { getCart, updateCartItemQty, removeFromCart, getCartTotal } from "@/lib/cart"
import { ShoppingBag } from "lucide-react"

export default function CartPage() {
  const navigate = useNavigate()
  const [cart, setCart] = useState([])

  useEffect(() => {
    setCart(getCart())
    const handleUpdate = () => setCart(getCart())
    window.addEventListener("cart-updated", handleUpdate)
    return () => window.removeEventListener("cart-updated", handleUpdate)
  }, [])

  const handleUpdateQty = (sku, qty) => {
    updateCartItemQty(sku, qty)
    setCart(getCart())
    window.dispatchEvent(new Event("cart-updated"))
  }

  const handleRemove = (sku) => {
    removeFromCart(sku)
    setCart(getCart())
    window.dispatchEvent(new Event("cart-updated"))
  }

  const subtotal = getCartTotal()
  const shipping = subtotal >= 499000 ? 0 : 30000

  if (cart.length === 0) {
    return (
      <PageLayout title="Giỏ hàng" breadcrumbs={[{ label: "Giỏ hàng" }]}>
        <div className="section">
          <div className="card p-12 text-center">
            <ShoppingBag className="w-16 h-16 mx-auto text-muted-foreground mb-4" />
            <div className="text-lg font-medium mb-2">Giỏ hàng trống</div>
            <div className="text-muted-foreground mb-6">Hãy thêm sản phẩm vào giỏ hàng để tiếp tục mua sắm.</div>
            <Button onClick={() => navigate("/products")}>Khám phá sản phẩm</Button>
          </div>
        </div>
      </PageLayout>
    )
  }

  return (
    <PageLayout title="Giỏ hàng" breadcrumbs={[{ label: "Giỏ hàng" }]}>
      <div className="section grid grid-cols-1 lg:grid-cols-3 gap-8">
        <div className="lg:col-span-2 space-y-4">
          {cart.map((item) => (
            <CartLineItem key={item.sku_variant} item={item} onUpdateQty={handleUpdateQty} onRemove={handleRemove} />
          ))}
        </div>
        <div className="lg:col-span-1">
          <CartSummary subtotal={subtotal} shipping={shipping} onCheckout={() => navigate("/checkout")} />
        </div>
      </div>
    </PageLayout>
  )
}
