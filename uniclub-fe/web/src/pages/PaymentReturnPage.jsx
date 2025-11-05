import React, { useState, useEffect } from "react"
import { useNavigate, useSearchParams } from "react-router-dom"
import { PageLayout } from "../components/PageLayout"
import { Button } from "@/components/ui/button"
import { CheckCircle2, XCircle, Loader2 } from "lucide-react"

export default function PaymentReturnPage() {
  const [searchParams] = useSearchParams()
  const navigate = useNavigate()
  const [loading, setLoading] = useState(true)
  const [paymentResult, setPaymentResult] = useState(null)

  useEffect(() => {
    const validatePayment = async () => {
      try {
        // Get all query params
        const params = {}
        for (const [key, value] of searchParams.entries()) {
          params[key] = value
        }

        // Call backend to validate
        const queryString = new URLSearchParams(params).toString()
        const response = await fetch(`http://localhost:8080/api/vnpay/return?${queryString}`)
        
        if (!response.ok) {
          throw new Error("Không thể xác thực thanh toán")
        }

        const data = await response.json()
        setPaymentResult(data)
      } catch (error) {
        console.error('Error validating payment:', error)
        setPaymentResult({
          success: false,
          message: "Có lỗi xảy ra khi xác thực thanh toán"
        })
      } finally {
        setLoading(false)
      }
    }

    validatePayment()
  }, [searchParams])

  if (loading) {
    return (
      <PageLayout title="Xác thực thanh toán" breadcrumbs={[{ label: "Xác thực thanh toán" }]}>
        <div className="section">
          <div className="flex items-center justify-center py-20">
            <Loader2 className="w-12 h-12 animate-spin text-primary" />
          </div>
        </div>
      </PageLayout>
    )
  }

  const isSuccess = paymentResult?.success === true

  return (
    <PageLayout 
      title={isSuccess ? "Thanh toán thành công" : "Thanh toán thất bại"} 
      breadcrumbs={[{ label: "Kết quả thanh toán" }]}
    >
      <div className="section">
        <div className="max-w-2xl mx-auto">
          <div className="card p-12 text-center">
            {isSuccess ? (
              <>
                <div 
                  className="w-20 h-20 rounded-full flex items-center justify-center mx-auto mb-6"
                  style={{ backgroundColor: "hsl(142 76% 36% / 0.1)" }}
                >
                  <CheckCircle2 className="w-12 h-12" style={{ color: "hsl(142 76% 36%)" }} />
                </div>
                <h1 className="text-2xl font-bold mb-2" style={{ color: "hsl(142 76% 36%)" }}>
                  Thanh toán thành công!
                </h1>
                <p className="text-muted-foreground mb-2">
                  {paymentResult.message}
                </p>
                {paymentResult.orderId && (
                  <p className="text-sm text-muted-foreground mb-6">
                    Mã đơn hàng: <span className="font-semibold">#{paymentResult.orderId}</span>
                    {paymentResult.transactionNo && (
                      <> • Mã giao dịch: <span className="font-semibold">{paymentResult.transactionNo}</span></>
                    )}
                  </p>
                )}
                <div className="flex gap-3 justify-center">
                  {paymentResult.orderId && (
                    <Button onClick={() => navigate(`/orders/${paymentResult.orderId}`)}>
                      Xem đơn hàng
                    </Button>
                  )}
                  <Button variant="outline" onClick={() => navigate("/orders")}>
                    Danh sách đơn hàng
                  </Button>
                  <Button variant="outline" onClick={() => navigate("/products")}>
                    Tiếp tục mua sắm
                  </Button>
                </div>
              </>
            ) : (
              <>
                <div 
                  className="w-20 h-20 rounded-full flex items-center justify-center mx-auto mb-6"
                  style={{ backgroundColor: "hsl(0 84.2% 60.2% / 0.1)" }}
                >
                  <XCircle className="w-12 h-12" style={{ color: "hsl(0 84.2% 60.2%)" }} />
                </div>
                <h1 className="text-2xl font-bold mb-2" style={{ color: "hsl(0 84.2% 60.2%)" }}>
                  Thanh toán thất bại
                </h1>
                <p className="text-muted-foreground mb-6">
                  {paymentResult?.message || "Giao dịch không thành công. Vui lòng thử lại."}
                </p>
                {paymentResult?.responseCode && (
                  <p className="text-sm text-muted-foreground mb-6">
                    Mã lỗi: {paymentResult.responseCode}
                  </p>
                )}
                <div className="flex gap-3 justify-center">
                  <Button onClick={() => navigate("/cart")}>
                    Quay lại giỏ hàng
                  </Button>
                  <Button variant="outline" onClick={() => navigate("/products")}>
                    Tiếp tục mua sắm
                  </Button>
                </div>
              </>
            )}
          </div>
        </div>
      </div>
    </PageLayout>
  )
}
