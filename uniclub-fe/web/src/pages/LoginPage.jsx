import React, { useState } from "react"
import { Link, useNavigate } from "react-router-dom"
import { PageLayout } from "../components/PageLayout"
import { Button } from "@/components/ui/button"
import { Input } from "@/components/ui/input"
import { Eye, EyeOff, LogIn } from "lucide-react"

export default function LoginPage() {
  const navigate = useNavigate()
  const [formData, setFormData] = useState({
    email: "",
    password: "",
  })
  const [showPassword, setShowPassword] = useState(false)
  const [error, setError] = useState("")
  const [isLoading, setIsLoading] = useState(false)

  const handleChange = (e) => {
    setFormData({ ...formData, [e.target.name]: e.target.value })
    setError("")
  }

  const handleSubmit = async (e) => {
    e.preventDefault()
    setError("")
    setIsLoading(true)

    try {
      // Call real backend API
      const response = await fetch('http://localhost:8080/api/auth/login', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify({
          email: formData.email,
          password: formData.password,
        }),
      })

      if (response.ok) {
        const data = await response.json()
        
        // Store user info and token
        const authUser = {
          id: data.id,
          email: data.email,
          full_name: data.fullName,
          role: data.role,
          token: data.token,
        }
        
        localStorage.setItem("uniclub_user", JSON.stringify(authUser))
        window.dispatchEvent(new Event("auth-updated"))
        
        // Redirect to home
        navigate("/")
      } else {
        const errorText = await response.text()
        setError(errorText || "Email hoặc mật khẩu không đúng")
      }
    } catch (err) {
      console.error('Login error:', err)
      setError("Lỗi kết nối. Vui lòng kiểm tra kết nối mạng.")
    } finally {
      setIsLoading(false)
    }
  }

  return (
    <PageLayout title="Đăng nhập" breadcrumbs={[{ label: "Đăng nhập" }]}>
      <div className="section flex items-center justify-center min-h-[calc(100vh-200px)]">
        <div className="w-full max-w-md">
          <div className="card p-8">
            <div className="text-center mb-8">
              <div className="w-16 h-16 rounded-full mx-auto mb-4 flex items-center justify-center" style={{ backgroundColor: "hsl(217.2 91.2% 55% / 0.1)" }}>
                <LogIn className="w-8 h-8" style={{ color: "hsl(217.2 91.2% 55%)" }} />
              </div>
              <h1 className="text-2xl font-bold text-foreground mb-2">Chào mừng trở lại</h1>
              <p className="text-muted-foreground">Đăng nhập để tiếp tục mua sắm</p>
            </div>

            <form onSubmit={handleSubmit} className="space-y-4">
              {error && (
                <div className="p-3 rounded-lg text-sm" style={{ backgroundColor: "hsl(0 84.2% 60.2% / 0.1)", color: "hsl(0 84.2% 60.2%)" }}>
                  {error}
                </div>
              )}

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
              </div>

              <div className="flex items-center justify-between text-sm">
                <label className="flex items-center gap-2 cursor-pointer">
                  <input type="checkbox" className="w-4 h-4 rounded border-border" />
                  <span className="text-muted-foreground">Ghi nhớ đăng nhập</span>
                </label>
                <Link to="/forgot-password" className="text-primary hover:underline">
                  Quên mật khẩu?
                </Link>
              </div>

              <Button type="submit" disabled={isLoading} className="w-full">
                {isLoading ? "Đang đăng nhập..." : "Đăng nhập"}
              </Button>

              <div className="text-center text-sm text-muted-foreground">
                Chưa có tài khoản?{" "}
                <Link to="/register" className="text-primary hover:underline font-medium">
                  Đăng ký ngay
                </Link>
              </div>
            </form>
          </div>
        </div>
      </div>
    </PageLayout>
  )
}