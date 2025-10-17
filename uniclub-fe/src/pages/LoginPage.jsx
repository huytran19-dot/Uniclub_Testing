import React, { useState } from "react"
import { Link, useNavigate } from "react-router-dom"
import { PageLayout } from "../components/PageLayout"
import { Button } from "@/components/ui/button"
import { Input } from "@/components/ui/input"
import { users } from "@/lib/mock-data"
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

  const handleSubmit = (e) => {
    e.preventDefault()
    setError("")
    setIsLoading(true)

    // Mock authentication
    setTimeout(() => {
      const user = users.find(
        (u) => u.email === formData.email && u.password === formData.password
      )

      if (user) {
        // Store user in localStorage with correct key
        const authUser = {
          id: user.id,
          email: user.email,
          full_name: user.full_name,
          id_role: user.id_role,
        }
        localStorage.setItem("uniclub_user", JSON.stringify(authUser))
        window.dispatchEvent(new Event("auth-updated"))
        navigate("/")
      } else {
        setError("Email hoặc mật khẩu không đúng")
      }
      setIsLoading(false)
    }, 800)
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

          <div className="mt-4 text-center text-xs text-muted-foreground">
            Demo: admin@uniclub.vn / admin123
          </div>
        </div>
      </div>
    </PageLayout>
  )
}
