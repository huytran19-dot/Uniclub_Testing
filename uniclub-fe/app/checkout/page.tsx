"use client"

import { useEffect, useState } from "react"
import { useRouter } from "next/navigation"
import { CheckCircle2 } from "lucide-react"
import { PageLayout } from "@/components/page-layout"
import { FormField } from "@/components/form-field"
import { VNAddressPicker } from "@/components/vn-address-picker"
import { Price } from "@/components/price"
import { Button } from "@/components/ui/button"
import { Textarea } from "@/components/ui/textarea"
import { RadioGroup, RadioGroupItem } from "@/components/ui/radio-group"
import { Label } from "@/components/ui/label"
import { getCart, getCartTotal, clearCart } from "@/lib/cart"
import { getCurrentUser } from "@/lib/auth"
import { payment_methods, orders, order_variants, billing_details } from "@/lib/mock-data"

interface BillingForm {
  full_name: string
  phone: string
  email: string
  address: string
  province: string
  district: string
  ward: string
  note: string
}

export default function CheckoutPage() {
  const router = useRouter()
  const [mounted, setMounted] = useState(false)
  const [step, setStep] = useState(1)
  const [selectedPayment, setSelectedPayment] = useState<number | null>(null)
  const [orderId, setOrderId] = useState<number | null>(null)

  const [billingForm, setBillingForm] = useState<BillingForm>({
    full_name: "",
    phone: "",
    email: "",
    address: "",
    province: "",
    district: "",
    ward: "",
    note: "",
  })

  const [errors, setErrors] = useState<Partial<Record<keyof BillingForm, string>>>({})

  useEffect(() => {
    setMounted(true)

    const user = getCurrentUser()
    if (!user) {
      router.push("/login?redirect=/checkout")
      return
    }

    const cart = getCart()
    if (cart.length === 0) {
      router.push("/cart")
      return
    }

    // Pre-fill user info
    setBillingForm((prev) => ({
      ...prev,
      full_name: user.full_name,
      email: user.email,
    }))

    // Get note from cart
    const savedNote = sessionStorage.getItem("order_note")
    if (savedNote) {
      setBillingForm((prev) => ({ ...prev, note: savedNote }))
    }
  }, [router])

  if (!mounted) return null

  const cart = getCart()
  const total = getCartTotal()
  const activePaymentMethods = payment_methods.filter((pm) => pm.status === 1)

  const validateBillingForm = (): boolean => {
    const newErrors: Partial<Record<keyof BillingForm, string>> = {}

    if (!billingForm.full_name.trim()) newErrors.full_name = "Vui lòng nhập họ tên"
    if (!billingForm.phone.trim()) newErrors.phone = "Vui lòng nhập số điện thoại"
    if (!billingForm.email.trim()) newErrors.email = "Vui lòng nhập email"
    if (!billingForm.address.trim()) newErrors.address = "Vui lòng nhập địa chỉ"
    if (!billingForm.province) newErrors.province = "Vui lòng chọn tỉnh/thành"
    if (!billingForm.district) newErrors.district = "Vui lòng chọn quận/huyện"
    if (!billingForm.ward) newErrors.ward = "Vui lòng chọn phường/xã"

    setErrors(newErrors)
    return Object.keys(newErrors).length === 0
  }

  const handleNextStep = () => {
    if (step === 1) {
      if (!validateBillingForm()) return
      setStep(2)
    } else if (step === 2) {
      if (!selectedPayment) return
      setStep(3)
    }
  }

  const handlePlaceOrder = () => {
    const user = getCurrentUser()
    if (!user) return

    // Create order
    const newOrderId = orders.length > 0 ? Math.max(...orders.map((o) => o.id)) + 1 : 1001

    const newOrder = {
      id: newOrderId,
      total,
      note: billingForm.note,
      id_payment: selectedPayment!,
      id_user: user.id,
      status: "PENDING",
      created_at: new Date().toISOString(),
      updated_at: new Date().toISOString(),
    }

    orders.push(newOrder)

    // Create order variants
    cart.forEach((item) => {
      order_variants.push({
        id_order: newOrderId,
        sku_variant: item.sku_variant,
        quantity: item.qty,
        price: item.unitPrice,
      })
    })

    // Create billing detail
    const newBillingDetail = {
      id: billing_details.length + 1,
      id_order: newOrderId,
      ...billingForm,
      status: 1,
      created_at: new Date().toISOString(),
      updated_at: new Date().toISOString(),
    }

    billing_details.push(newBillingDetail)

    // Clear cart
    clearCart()
    sessionStorage.removeItem("order_note")
    window.dispatchEvent(new Event("cart-updated"))

    setOrderId(newOrderId)
    setStep(4)
  }

  // Success page
  if (step === 4 && orderId) {
    return (
      <PageLayout title="Đặt hàng thành công" breadcrumbs={[{ label: "Thanh toán" }]}>
        <div className="max-w-2xl mx-auto text-center py-16">
          <CheckCircle2 className="w-20 h-20 text-success mx-auto mb-6" />
          <h2 className="text-3xl font-bold text-foreground mb-4">Đặt hàng thành công!</h2>
          <p className="text-lg text-muted-foreground mb-2">Mã đơn hàng: #{orderId}</p>
          <p className="text-muted-foreground mb-8">Cảm ơn bạn đã mua hàng tại UniClub</p>

          <div className="flex gap-4 justify-center">
            <Button onClick={() => router.push(`/orders/${orderId}`)}>Xem đơn hàng</Button>
            <Button variant="outline" onClick={() => router.push("/products")} className="bg-transparent">
              Tiếp tục mua sắm
            </Button>
          </div>
        </div>
      </PageLayout>
    )
  }

  return (
    <PageLayout title="Thanh toán" breadcrumbs={[{ label: "Giỏ hàng", href: "/cart" }, { label: "Thanh toán" }]}>
      <div className="grid grid-cols-1 lg:grid-cols-3 gap-8">
        {/* Main Content */}
        <div className="lg:col-span-2">
          {/* Step Indicator */}
          <div className="flex items-center justify-center mb-8">
            {[1, 2, 3].map((s) => (
              <div key={s} className="flex items-center">
                <div
                  className={`w-10 h-10 rounded-full flex items-center justify-center font-semibold ${
                    s <= step ? "bg-primary text-white" : "bg-surface text-muted-foreground"
                  }`}
                >
                  {s}
                </div>
                {s < 3 && <div className={`w-16 h-1 ${s < step ? "bg-primary" : "bg-surface"}`} />}
              </div>
            ))}
          </div>

          {/* Step 1: Billing Info */}
          {step === 1 && (
            <div className="bg-white rounded-2xl border border-border p-6 shadow-sm">
              <h3 className="text-xl font-semibold text-foreground mb-6">Thông tin giao hàng</h3>

              <div className="space-y-4">
                <FormField
                  name="full_name"
                  label="Họ và tên"
                  value={billingForm.full_name}
                  onChange={(value) => setBillingForm({ ...billingForm, full_name: value })}
                  error={errors.full_name}
                  required
                />

                <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
                  <FormField
                    name="phone"
                    label="Số điện thoại"
                    type="tel"
                    value={billingForm.phone}
                    onChange={(value) => setBillingForm({ ...billingForm, phone: value })}
                    error={errors.phone}
                    required
                  />

                  <FormField
                    name="email"
                    label="Email"
                    type="email"
                    value={billingForm.email}
                    onChange={(value) => setBillingForm({ ...billingForm, email: value })}
                    error={errors.email}
                    required
                  />
                </div>

                <FormField
                  name="address"
                  label="Địa chỉ"
                  value={billingForm.address}
                  onChange={(value) => setBillingForm({ ...billingForm, address: value })}
                  error={errors.address}
                  required
                  placeholder="Số nhà, tên đường"
                />

                <VNAddressPicker
                  province={billingForm.province}
                  district={billingForm.district}
                  ward={billingForm.ward}
                  onChange={(address) => setBillingForm({ ...billingForm, ...address })}
                  errors={errors}
                />

                <div>
                  <Label htmlFor="note" className="text-sm font-medium text-foreground mb-2 block">
                    Ghi chú
                  </Label>
                  <Textarea
                    id="note"
                    placeholder="Ghi chú thêm (tùy chọn)"
                    value={billingForm.note}
                    onChange={(e) => setBillingForm({ ...billingForm, note: e.target.value })}
                    rows={3}
                  />
                </div>
              </div>

              <Button size="lg" className="w-full mt-6" onClick={handleNextStep}>
                Tiếp tục
              </Button>
            </div>
          )}

          {/* Step 2: Payment Method */}
          {step === 2 && (
            <div className="bg-white rounded-2xl border border-border p-6 shadow-sm">
              <h3 className="text-xl font-semibold text-foreground mb-6">Phương thức thanh toán</h3>

              <RadioGroup
                value={selectedPayment?.toString()}
                onValueChange={(value) => setSelectedPayment(Number(value))}
              >
                <div className="space-y-3">
                  {activePaymentMethods.map((method) => (
                    <div key={method.id} className="flex items-center space-x-3 border border-border rounded-lg p-4">
                      <RadioGroupItem value={method.id.toString()} id={`payment-${method.id}`} />
                      <Label htmlFor={`payment-${method.id}`} className="flex-1 cursor-pointer">
                        <div className="font-medium text-foreground">{method.name}</div>
                        <div className="text-sm text-muted-foreground">{method.description}</div>
                      </Label>
                    </div>
                  ))}
                </div>
              </RadioGroup>

              <div className="flex gap-4 mt-6">
                <Button variant="outline" size="lg" className="flex-1 bg-transparent" onClick={() => setStep(1)}>
                  Quay lại
                </Button>
                <Button size="lg" className="flex-1" onClick={handleNextStep} disabled={!selectedPayment}>
                  Tiếp tục
                </Button>
              </div>
            </div>
          )}

          {/* Step 3: Review */}
          {step === 3 && (
            <div className="space-y-6">
              <div className="bg-white rounded-2xl border border-border p-6 shadow-sm">
                <h3 className="text-xl font-semibold text-foreground mb-4">Xác nhận đơn hàng</h3>

                <div className="space-y-4">
                  <div>
                    <h4 className="font-medium text-foreground mb-2">Thông tin giao hàng</h4>
                    <div className="text-sm text-muted-foreground space-y-1">
                      <p>{billingForm.full_name}</p>
                      <p>{billingForm.phone}</p>
                      <p>{billingForm.email}</p>
                      <p>
                        {billingForm.address}, {billingForm.ward}, {billingForm.district}, {billingForm.province}
                      </p>
                    </div>
                  </div>

                  <div>
                    <h4 className="font-medium text-foreground mb-2">Phương thức thanh toán</h4>
                    <p className="text-sm text-muted-foreground">
                      {activePaymentMethods.find((pm) => pm.id === selectedPayment)?.name}
                    </p>
                  </div>
                </div>
              </div>

              <div className="flex gap-4">
                <Button variant="outline" size="lg" className="flex-1 bg-transparent" onClick={() => setStep(2)}>
                  Quay lại
                </Button>
                <Button size="lg" className="flex-1" onClick={handlePlaceOrder}>
                  Đặt hàng
                </Button>
              </div>
            </div>
          )}
        </div>

        {/* Order Summary */}
        <div className="lg:col-span-1">
          <div className="bg-white rounded-2xl border border-border p-6 shadow-sm sticky top-20">
            <h3 className="text-lg font-semibold text-foreground mb-4">Đơn hàng ({cart.length} sản phẩm)</h3>

            <div className="space-y-3 mb-6 max-h-64 overflow-y-auto">
              {cart.map((item) => (
                <div key={item.sku_variant} className="flex gap-3 text-sm">
                  <div className="flex-1">
                    <div className="font-medium text-foreground">{item.productName}</div>
                    <div className="text-muted-foreground">
                      {item.sizeName} / {item.colorName} × {item.qty}
                    </div>
                  </div>
                  <Price value={item.unitPrice * item.qty} className="font-medium text-foreground" />
                </div>
              ))}
            </div>

            <div className="border-t border-border pt-4">
              <div className="flex justify-between items-center mb-2">
                <span className="text-lg font-semibold text-foreground">Tổng cộng</span>
                <Price value={total} className="text-2xl font-bold text-primary" />
              </div>
            </div>
          </div>
        </div>
      </div>
    </PageLayout>
  )
}
