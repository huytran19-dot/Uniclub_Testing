import React, { useState, useEffect } from "react"
import { useNavigate } from "react-router-dom"
import { PageLayout } from "../components/PageLayout"
import { CartLineItem } from "@/components/cart/CartLineItem"
import { CartSummary } from "@/components/cart/CartSummary"
import { Button } from "@/components/ui/button"
import { getFullCart, updateCartItem, removeCartItem } from "@/lib/cart-api"
import { ShoppingBag, Loader2 } from "lucide-react"

export default function CartPage() {
  const navigate = useNavigate()
  const [cart, setCart] = useState([])
  const [loading, setLoading] = useState(true)

  const fetchCart = async () => {
    const userStr = localStorage.getItem('uniclub_user')
    if (!userStr) {
      setLoading(false)
      return
    }

    try {
      const user = JSON.parse(userStr)
      const { items } = await getFullCart(user.id)
      setCart(items)
    } catch (error) {
      console.error('Error fetching cart:', error)
    } finally {
      setLoading(false)
    }
  }

  useEffect(() => {
    fetchCart()
    const handleUpdate = () => fetchCart()
    window.addEventListener("cart-updated", handleUpdate)
    return () => window.removeEventListener("cart-updated", handleUpdate)
  }, [])

  const handleUpdateQty = async (sku, qty) => {
    try {
      const item = cart.find(i => i.sku_variant === sku)
      if (!item) return
      
      // ✅ Kiểm tra số lượng mới có vượt tồn kho không
      if (qty > item.maxQuantity) {
        alert(`⚠️ Không thể tăng thêm!\n\nSố lượng tồn kho chỉ còn: ${item.maxQuantity}\nSố lượng bạn đang chọn: ${qty}\n\nĐã tự động giảm xuống mức tối đa.`)
        // Tự động set về số lượng tối đa
        await updateCartItem(item.id, item.maxQuantity)
      } else if (qty < 1) {
        // Nếu giảm xuống 0 → Xóa khỏi giỏ
        await removeCartItem(item.id)
      } else {
        // Update bình thường
        await updateCartItem(item.id, qty)
      }
      
      fetchCart()
      window.dispatchEvent(new Event("cart-updated"))
    } catch (error) {
      console.error('Error updating cart item:', error)
      alert(error.response?.data?.message || 'Không thể cập nhật số lượng')
    }
  }

  const handleRemove = async (sku) => {
    try {
      const item = cart.find(i => i.sku_variant === sku)
      if (!item) return
      
      await removeCartItem(item.id)
      fetchCart()
      window.dispatchEvent(new Event("cart-updated"))
    } catch (error) {
      console.error('Error removing cart item:', error)
      alert(error.response?.data?.message || 'Không thể xóa sản phẩm')
    }
  }

  const subtotal = cart.reduce((sum, item) => sum + (item.subtotal || item.unitPrice * item.quantity), 0)
  const shipping = subtotal >= 499000 ? 0 : 30000

  if (loading) {
    return (
      <PageLayout title="Giỏ hàng" breadcrumbs={[{ label: "Giỏ hàng" }]}>
        <div className="section">
          <div className="flex items-center justify-center py-20">
            <Loader2 className="w-12 h-12 animate-spin text-primary" />
          </div>
        </div>
      </PageLayout>
    )
  }

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
