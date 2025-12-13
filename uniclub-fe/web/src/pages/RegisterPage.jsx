import React, { useState } from "react"
import { Link, useNavigate } from "react-router-dom"
import { PageLayout } from "../components/PageLayout"
import { Button } from "@/components/ui/button"
import { Input } from "@/components/ui/input"
import { Eye, EyeOff, UserPlus } from "lucide-react"

export default function RegisterPage() {
  const navigate = useNavigate()
  const [formData, setFormData] = useState({
    full_name: "",
    email: "",
    phone: "",
    password: "",
    confirmPassword: "",
  })
  const [showPassword, setShowPassword] = useState(false)
  const [showConfirmPassword, setShowConfirmPassword] = useState(false)
  const [error, setError] = useState("")
  const [isLoading, setIsLoading] = useState(false)

  const handleChange = (e) => {
    setFormData({ ...formData, [e.target.name]: e.target.value })
    setError("")
  }

  const handleSubmit = async (e) => {
    e.preventDefault()
    setError("")

    // Validation
    if (formData.password.length < 6) {
      setError("Mật khẩu phải có ít nhất 6 ký tự")
      return
    }

    if (formData.password !== formData.confirmPassword) {
      setError("Mật khẩu xác nhận không khớp")
      return
    }

    if (!/^[0-9]{10}$/.test(formData.phone)) {
      setError("Số điện thoại không hợp lệ (10 chữ số)")
      return
    }

    setIsLoading(true)

    try {
      // Call real backend API
      const API_URL = import.meta.env.VITE_API_URL || 'http://localhost:8080/api'
      const response = await fetch(`${API_URL}/auth/register`, {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify({
          email: formData.email,
          password: formData.password,
          fullname: formData.full_name,
        }),
      })

      if (response.ok) {
        // Registration successful - redirect to verify email page
        navigate('/verify-email', { state: { email: formData.email } })
      } else {
        const errorText = await response.text()
        setError(errorText || 'Đăng ký thất bại. Vui lòng thử lại.')
      }
    } catch (err) {
      console.error('Registration error:', err)
      setError('Lỗi kết nối. Vui lòng kiểm tra kết nối mạng.')
    } finally {
      setIsLoading(false)
    }
  }

  return (
    <PageLayout title="Đăng ký" breadcrumbs={[{ label: "Đăng ký" }]}>
      <div className="section flex items-center justify-center min-h-[calc(100vh-200px)]">
        <div className="w-full max-w-md">
          <div className="card p-8">
            <div className="text-center mb-8">
              <div className="w-16 h-16 rounded-full mx-auto mb-4 flex items-center justify-center" style={{ backgroundColor: "hsl(142 76% 36% / 0.1)" }}>
                <UserPlus className="w-8 h-8" style={{ color: "hsl(142 76% 36%)" }} />
              </div>
              <h1 className="text-2xl font-bold text-foreground mb-2">Tạo tài khoản mới</h1>
              <p className="text-muted-foreground">Đăng ký để bắt đầu mua sắm</p>
            </div>

            <form onSubmit={handleSubmit} className="space-y-4">
              {error && (
                <div className="p-3 rounded-lg text-sm" style={{ backgroundColor: "hsl(0 84.2% 60.2% / 0.1)", color: "hsl(0 84.2% 60.2%)" }}>
                  {error}
                </div>
              )}

              <div>
                <label className="block text-sm font-medium text-foreground mb-1">
                  Họ và tên *
                </label>
                <Input
                  type="text"
                  name="full_name"
                  value={formData.full_name}
                  onChange={handleChange}
                  placeholder="Nguyễn Văn A"
                  required
                />
              </div>

              <div>
                <label className="block text-sm font-medium text-foreground mb-1">
                  Email *
                </label>
                <Input
                  type="email"
                  name="email"
                  value={formData.email}
                  onChange={handleChange}
                  placeholder="email@example.com"
                  required
                />
              </div>

              <div>
                <label className="block text-sm font-medium text-foreground mb-1">
                  Số điện thoại *
                </label>
                <Input
                  type="tel"
                  name="phone"
                  value={formData.phone}
                  onChange={handleChange}
                  placeholder="0901234567"
                  required
                />
              </div>

              <div>
                <label className="block text-sm font-medium text-foreground mb-1">
                  Mật khẩu *
                </label>
                <div className="relative">
                  <button
                    type="button"
                    onClick={() => setShowPassword(!showPassword)}
                    className="absolute left-3 top-1/2 -translate-y-1/2 text-muted-foreground hover:text-foreground z-10"
                  >
                    {showPassword ? <EyeOff className="w-4 h-4" /> : <Eye className="w-4 h-4" />}
                  </button>
                  <Input
                    type={showPassword ? "text" : "password"}
                    name="password"
                    value={formData.password}
                    onChange={handleChange}
                    placeholder="••••••••"
                    className="pl-10"
                    required
                  />
                </div>
                <div className="text-xs text-muted-foreground mt-1">
                  Tối thiểu 6 ký tự
                </div>
              </div>

              <div>
                <label className="block text-sm font-medium text-foreground mb-1">
                  Xác nhận mật khẩu *
                </label>
                <div className="relative">
                  <button
                    type="button"
                    onClick={() => setShowConfirmPassword(!showConfirmPassword)}
                    className="absolute left-3 top-1/2 -translate-y-1/2 text-muted-foreground hover:text-foreground z-10"
                  >
                    {showConfirmPassword ? <EyeOff className="w-4 h-4" /> : <Eye className="w-4 h-4" />}
                  </button>
                  <Input
                    type={showConfirmPassword ? "text" : "password"}
                    name="confirmPassword"
                    value={formData.confirmPassword}
                    onChange={handleChange}
                    placeholder="••••••••"
                    className="pl-10"
                    required
                  />
                </div>
              </div>

              <div className="flex items-start gap-2 text-sm">
                <input type="checkbox" required className="w-4 h-4 mt-0.5 rounded border-border" />
                <span className="text-muted-foreground">
                  Tôi đồng ý với{" "}
                  <Link to="/terms" className="text-primary hover:underline">
                    Điều khoản dịch vụ
                  </Link>{" "}
                  và{" "}
                  <Link to="/privacy" className="text-primary hover:underline">
                    Chính sách bảo mật
                  </Link>
                </span>
              </div>

              <Button type="submit" disabled={isLoading} className="w-full" variant="secondary">
                {isLoading ? "Đang đăng ký..." : "Đăng ký"}
              </Button>

              <div className="text-center text-sm text-muted-foreground">
                Đã có tài khoản?{" "}
                <Link to="/login" className="text-primary hover:underline font-medium">
                  Đăng nhập ngay
                </Link>
              </div>
            </form>
          </div>
        </div>
      </div>
    </PageLayout>
  )
}
