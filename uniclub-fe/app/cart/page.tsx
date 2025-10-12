"use client"

import { useEffect, useState } from "react"
import { useRouter } from "next/navigation"
import Image from "next/image"
import { Trash2, Plus, Minus, ShoppingBag } from "lucide-react"
import { PageLayout } from "@/components/page-layout"
import { Price } from "@/components/price"
import { Button } from "@/components/ui/button"
import { Textarea } from "@/components/ui/textarea"
import { getCart, updateCartItemQty, removeFromCart, getCartTotal, type CartItem } from "@/lib/cart"

export default function CartPage() {
  const router = useRouter()
  const [mounted, setMounted] = useState(false)
  const [cart, setCart] = useState<CartItem[]>([])
  const [note, setNote] = useState("")

  useEffect(() => {
    setMounted(true)
    setCart(getCart())

    const handleCartUpdate = () => {
      setCart(getCart())
    }

    window.addEventListener("cart-updated", handleCartUpdate)
    return () => window.removeEventListener("cart-updated", handleCartUpdate)
  }, [])

  if (!mounted) return null

  const handleUpdateQty = (sku: number, newQty: number) => {
    updateCartItemQty(sku, newQty)
    setCart(getCart())
    window.dispatchEvent(new Event("cart-updated"))
  }

  const handleRemove = (sku: number) => {
    removeFromCart(sku)
    setCart(getCart())
    window.dispatchEvent(new Event("cart-updated"))
  }

  const handleCheckout = () => {
    if (cart.length === 0) return
    // Store note in sessionStorage for checkout
    if (note) {
      sessionStorage.setItem("order_note", note)
    }
    router.push("/checkout")
  }

  const total = getCartTotal()

  if (cart.length === 0) {
    return (
      <PageLayout title="Giỏ hàng" breadcrumbs={[{ label: "Giỏ hàng" }]}>
        <div className="text-center py-16">
          <ShoppingBag className="w-16 h-16 mx-auto text-muted-foreground mb-4" />
          <h2 className="text-2xl font-semibold text-foreground mb-2">Giỏ hàng trống</h2>
          <p className="text-muted-foreground mb-6">Hãy thêm sản phẩm vào giỏ hàng để tiếp tục mua sắm</p>
          <Button onClick={() => router.push("/products")}>Khám phá sản phẩm</Button>
        </div>
      </PageLayout>
    )
  }

  return (
    <PageLayout title="Giỏ hàng" breadcrumbs={[{ label: "Giỏ hàng" }]}>
      <div className="grid grid-cols-1 lg:grid-cols-3 gap-8">
        {/* Cart Items */}
        <div className="lg:col-span-2 space-y-4">
          {cart.map((item) => (
            <div key={item.sku_variant} className="bg-white rounded-2xl border border-border p-6 shadow-sm">
              <div className="flex gap-4">
                {/* Image */}
                <div className="w-24 h-24 flex-shrink-0 rounded-lg overflow-hidden bg-surface">
                  <Image
                    src={item.image || "/placeholder.svg"}
                    alt={item.productName}
                    width={96}
                    height={96}
                    className="w-full h-full object-cover"
                  />
                </div>

                {/* Info */}
                <div className="flex-1 min-w-0">
                  <h3 className="font-semibold text-foreground mb-1">{item.productName}</h3>
                  <div className="text-sm text-muted-foreground mb-2">
                    {item.sizeName} / {item.colorName}
                  </div>
                  <Price value={item.unitPrice} className="text-lg font-bold text-primary" />
                </div>

                {/* Quantity Controls */}
                <div className="flex flex-col items-end gap-4">
                  <Button variant="ghost" size="icon" onClick={() => handleRemove(item.sku_variant)}>
                    <Trash2 className="w-4 h-4 text-danger" />
                  </Button>

                  <div className="flex items-center gap-2">
                    <Button
                      variant="outline"
                      size="icon"
                      className="h-8 w-8 bg-transparent"
                      onClick={() => handleUpdateQty(item.sku_variant, item.qty - 1)}
                      disabled={item.qty <= 1}
                    >
                      <Minus className="w-3 h-3" />
                    </Button>
                    <span className="text-sm font-medium w-8 text-center">{item.qty}</span>
                    <Button
                      variant="outline"
                      size="icon"
                      className="h-8 w-8 bg-transparent"
                      onClick={() => handleUpdateQty(item.sku_variant, item.qty + 1)}
                      disabled={item.qty >= item.maxQuantity}
                    >
                      <Plus className="w-3 h-3" />
                    </Button>
                  </div>

                  <div className="text-sm text-muted-foreground">
                    Tổng: <Price value={item.unitPrice * item.qty} className="font-semibold text-foreground" />
                  </div>
                </div>
              </div>
            </div>
          ))}

          {/* Note */}
          <div className="bg-white rounded-2xl border border-border p-6 shadow-sm">
            <label className="text-sm font-medium text-foreground mb-2 block">Ghi chú đơn hàng</label>
            <Textarea
              placeholder="Thêm ghi chú cho đơn hàng (tùy chọn)"
              value={note}
              onChange={(e) => setNote(e.target.value)}
              rows={3}
            />
          </div>
        </div>

        {/* Summary */}
        <div className="lg:col-span-1">
          <div className="bg-white rounded-2xl border border-border p-6 shadow-sm sticky top-20">
            <h3 className="text-lg font-semibold text-foreground mb-4">Tóm tắt đơn hàng</h3>

            <div className="space-y-3 mb-6">
              <div className="flex justify-between text-sm">
                <span className="text-muted-foreground">Tạm tính</span>
                <Price value={total} className="font-medium text-foreground" />
              </div>
              <div className="flex justify-between text-sm">
                <span className="text-muted-foreground">Phí vận chuyển</span>
                <span className="text-muted-foreground">Miễn phí</span>
              </div>
            </div>

            <div className="border-t border-border pt-4 mb-6">
              <div className="flex justify-between items-center">
                <span className="text-lg font-semibold text-foreground">Tổng cộng</span>
                <Price value={total} className="text-2xl font-bold text-primary" />
              </div>
            </div>

            <Button size="lg" className="w-full" onClick={handleCheckout}>
              Tiến hành thanh toán
            </Button>

            <Button
              variant="outline"
              size="lg"
              className="w-full mt-3 bg-transparent"
              onClick={() => router.push("/products")}
            >
              Tiếp tục mua sắm
            </Button>
          </div>
        </div>
      </div>
    </PageLayout>
  )
}
