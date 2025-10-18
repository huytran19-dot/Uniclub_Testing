import React from "react"
import { Price } from "@/components/Price"
import { Button } from "@/components/ui/button"

export function CartSummary({ subtotal, shipping = 0, discount = 0, onCheckout }) {
  const total = subtotal + shipping - discount

  return (
    <div className="card p-6 space-y-4">
      <h3 className="text-lg font-semibold text-foreground">Tóm tắt đơn hàng</h3>
      
      <div className="space-y-2 text-sm">
        <div className="flex justify-between">
          <span className="text-muted-foreground">Tạm tính</span>
          <Price value={subtotal} />
        </div>
        <div className="flex justify-between">
          <span className="text-muted-foreground">Phí vận chuyển</span>
          {shipping === 0 ? <span className="text-green-600">Miễn phí</span> : <Price value={shipping} />}
        </div>
        {discount > 0 && (
          <div className="flex justify-between text-green-600">
            <span>Giảm giá</span>
            <span>-<Price value={discount} /></span>
          </div>
        )}
      </div>

      <div className="pt-4 border-t border-border flex justify-between items-center">
        <span className="font-semibold text-foreground">Tổng cộng</span>
        <span className="text-xl font-bold text-primary">
          <Price value={total} />
        </span>
      </div>

      <Button onClick={onCheckout} className="w-full">
        Thanh toán
      </Button>

      <div className="text-xs text-muted-foreground text-center">
        Miễn phí vận chuyển cho đơn hàng từ 499.000đ
      </div>
    </div>
  )
}
