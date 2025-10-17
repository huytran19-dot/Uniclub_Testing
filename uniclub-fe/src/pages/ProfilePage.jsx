import React, { useState, useEffect } from "react"
import { PageLayout } from "../components/PageLayout"
import { Button } from "@/components/ui/button"
import { Input } from "@/components/ui/input"
import { getCurrentUser } from "@/lib/auth"
import { User, Mail, Calendar, Shield, Edit2, Save, X, Eye, EyeOff, Lock } from "lucide-react"
import { useNavigate } from "react-router-dom"
import { users } from "@/lib/mock-data"

export default function ProfilePage() {
  const navigate = useNavigate()
  const [user, setUser] = useState(null)
  const [isEditing, setIsEditing] = useState(false)
  const [showPasswordModal, setShowPasswordModal] = useState(false)
  const [formData, setFormData] = useState({
    full_name: "",
    email: "",
  })
  const [passwordData, setPasswordData] = useState({
    currentPassword: "",
    newPassword: "",
    confirmPassword: "",
  })
  const [showPasswords, setShowPasswords] = useState({
    current: false,
    new: false,
    confirm: false,
  })
  const [passwordError, setPasswordError] = useState("")
  const [passwordSuccess, setPasswordSuccess] = useState(false)

  useEffect(() => {
    const currentUser = getCurrentUser()
    if (!currentUser) {
      navigate("/login")
      return
    }
    setUser(currentUser)
    setFormData({
      full_name: currentUser.full_name || "",
      email: currentUser.email || "",
    })
  }, [navigate])

  const handleChange = (e) => {
    setFormData({ ...formData, [e.target.name]: e.target.value })
  }

  const handleSave = () => {
    // Update user in localStorage
    const updatedUser = { ...user, ...formData }
    localStorage.setItem("uniclub_user", JSON.stringify(updatedUser))
    setUser(updatedUser)
    setIsEditing(false)
    window.dispatchEvent(new Event("auth-updated"))
  }

  const handleCancel = () => {
    setFormData({
      full_name: user.full_name || "",
      email: user.email || "",
    })
    setIsEditing(false)
  }

  const handlePasswordChange = (e) => {
    setPasswordData({ ...passwordData, [e.target.name]: e.target.value })
    setPasswordError("")
  }

  const togglePasswordVisibility = (field) => {
    setShowPasswords({ ...showPasswords, [field]: !showPasswords[field] })
  }

  const handleChangePassword = () => {
    setPasswordError("")
    setPasswordSuccess(false)

    // Validation
    if (!passwordData.currentPassword || !passwordData.newPassword || !passwordData.confirmPassword) {
      setPasswordError("Vui lòng điền đầy đủ thông tin")
      return
    }

    if (passwordData.newPassword.length < 6) {
      setPasswordError("Mật khẩu mới phải có ít nhất 6 ký tự")
      return
    }

    if (passwordData.newPassword !== passwordData.confirmPassword) {
      setPasswordError("Mật khẩu xác nhận không khớp")
      return
    }

    // Find user in mock data
    const mockUser = users.find((u) => u.email === user.email)
    
    if (!mockUser || mockUser.password !== passwordData.currentPassword) {
      setPasswordError("Mật khẩu hiện tại không đúng")
      return
    }

    // Update password in mock data (in real app, this would be an API call)
    mockUser.password = passwordData.newPassword

    // Show success message
    setPasswordSuccess(true)
    setPasswordData({
      currentPassword: "",
      newPassword: "",
      confirmPassword: "",
    })

    // Close modal after 2 seconds
    setTimeout(() => {
      setShowPasswordModal(false)
      setPasswordSuccess(false)
    }, 2000)
  }

  const getRoleName = (id_role) => {
    const roles = {
      1: "Admin",
      2: "Customer",
    }
    return roles[id_role] || "Customer"
  }

  const formatDate = (dateString) => {
    if (!dateString) return "N/A"
    return new Date(dateString).toLocaleDateString("vi-VN", {
      year: "numeric",
      month: "long",
      day: "numeric",
    })
  }

  if (!user) {
    return null
  }

  return (
    <PageLayout
      title="Thông tin cá nhân"
      breadcrumbs={[{ label: "Thông tin cá nhân" }]}
    >
      <div className="section">
        <div className="max-w-4xl mx-auto">
          {/* Profile Header */}
          <div className="card p-8 mb-6">
            <div className="flex items-start justify-between mb-6">
              <div className="flex items-center gap-4">
                <div
                  className="w-20 h-20 rounded-full flex items-center justify-center text-white font-bold text-3xl"
                  style={{ backgroundColor: "hsl(217.2 91.2% 55%)" }}
                >
                  {user.full_name?.charAt(0).toUpperCase() || "U"}
                </div>
                <div>
                  <h1 className="text-2xl font-bold text-foreground mb-1">
                    {user.full_name || "Người dùng"}
                  </h1>
                  <div className="flex items-center gap-2">
                    <span
                      className="badge badge-primary"
                    >
                      {getRoleName(user.id_role)}
                    </span>
                    {user.status === 1 && (
                      <span className="badge badge-success">Hoạt động</span>
                    )}
                  </div>
                </div>
              </div>
              {!isEditing ? (
                <Button
                  variant="outline"
                  onClick={() => setIsEditing(true)}
                  className="gap-2"
                >
                  <Edit2 className="w-4 h-4" />
                  Chỉnh sửa
                </Button>
              ) : (
                <div className="flex gap-2">
                  <Button
                    variant="success"
                    onClick={handleSave}
                    className="gap-2"
                  >
                    <Save className="w-4 h-4" />
                    Lưu
                  </Button>
                  <Button
                    variant="outline"
                    onClick={handleCancel}
                    className="gap-2"
                  >
                    <X className="w-4 h-4" />
                    Hủy
                  </Button>
                </div>
              )}
            </div>

            {/* Profile Information */}
            <div className="grid md:grid-cols-2 gap-6">
              {/* Full Name */}
              <div>
                <label className="flex items-center gap-2 text-sm font-medium text-muted-foreground mb-2">
                  <User className="w-4 h-4" />
                  Họ và tên
                </label>
                {isEditing ? (
                  <Input
                    type="text"
                    name="full_name"
                    value={formData.full_name}
                    onChange={handleChange}
                    placeholder="Nhập họ và tên"
                  />
                ) : (
                  <div className="text-foreground font-medium">
                    {user.full_name || "Chưa cập nhật"}
                  </div>
                )}
              </div>

              {/* Email */}
              <div>
                <label className="flex items-center gap-2 text-sm font-medium text-muted-foreground mb-2">
                  <Mail className="w-4 h-4" />
                  Email
                </label>
                {isEditing ? (
                  <Input
                    type="email"
                    name="email"
                    value={formData.email}
                    onChange={handleChange}
                    placeholder="Nhập email"
                  />
                ) : (
                  <div className="text-foreground font-medium">
                    {user.email || "Chưa cập nhật"}
                  </div>
                )}
              </div>

              {/* Role */}
              <div className="md:col-span-2">
                <label className="flex items-center gap-2 text-sm font-medium text-muted-foreground mb-2">
                  <Shield className="w-4 h-4" />
                  Vai trò
                </label>
                <div className="text-foreground font-medium">
                  {getRoleName(user.id_role)}
                </div>
              </div>
            </div>
          </div>

          {/* Account Settings */}
          <div className="card p-8">
            <h2 className="text-xl font-bold text-foreground mb-6">
              Cài đặt tài khoản
            </h2>
            <div className="space-y-4">
              <div className="flex items-center justify-between p-4 border border-border rounded-lg">
                <div>
                  <div className="font-medium text-foreground mb-1">
                    Đổi mật khẩu
                  </div>
                  <div className="text-sm text-muted-foreground">
                    Cập nhật mật khẩu của bạn để bảo mật tài khoản
                  </div>
                </div>
                <Button variant="outline" onClick={() => setShowPasswordModal(true)}>
                  Đổi mật khẩu
                </Button>
              </div>

              <div className="flex items-center justify-between p-4 border border-border rounded-lg">
                <div>
                  <div className="font-medium text-foreground mb-1">
                    Lịch sử đơn hàng
                  </div>
                  <div className="text-sm text-muted-foreground">
                    Xem tất cả đơn hàng của bạn
                  </div>
                </div>
                <Button variant="outline" onClick={() => navigate("/orders")}>
                  Xem đơn hàng
                </Button>
              </div>
            </div>
          </div>
        </div>

        {/* Change Password Modal */}
        {showPasswordModal && (
          <div className="fixed inset-0 bg-black/50 flex items-center justify-center z-50 p-4">
            <div className="bg-white rounded-2xl shadow-xl max-w-md w-full p-6 animate-slide-up">
              <div className="flex items-center justify-between mb-6">
                <div className="flex items-center gap-3">
                  <div className="w-10 h-10 rounded-full flex items-center justify-center" style={{ backgroundColor: "hsl(217.2 91.2% 55% / 0.1)" }}>
                    <Lock className="w-5 h-5" style={{ color: "hsl(217.2 91.2% 55%)" }} />
                  </div>
                  <h2 className="text-xl font-bold text-foreground">Đổi mật khẩu</h2>
                </div>
                <button
                  onClick={() => {
                    setShowPasswordModal(false)
                    setPasswordError("")
                    setPasswordSuccess(false)
                    setPasswordData({
                      currentPassword: "",
                      newPassword: "",
                      confirmPassword: "",
                    })
                  }}
                  className="text-muted-foreground hover:text-foreground"
                >
                  <X className="w-5 h-5" />
                </button>
              </div>

              {passwordSuccess ? (
                <div className="p-4 rounded-lg mb-4" style={{ backgroundColor: "hsl(142 76% 36% / 0.1)", color: "hsl(142 76% 36%)" }}>
                  Đổi mật khẩu thành công!
                </div>
              ) : (
                <>
                  {passwordError && (
                    <div className="p-3 rounded-lg text-sm mb-4" style={{ backgroundColor: "hsl(0 84.2% 60.2% / 0.1)", color: "hsl(0 84.2% 60.2%)" }}>
                      {passwordError}
                    </div>
                  )}

                  <div className="space-y-4">
                    {/* Current Password */}
                    <div>
                      <label className="block text-sm font-medium text-foreground mb-1">
                        Mật khẩu hiện tại *
                      </label>
                      <div className="relative">
                        <button
                          type="button"
                          onClick={() => togglePasswordVisibility("current")}
                          className="absolute left-3 top-1/2 -translate-y-1/2 text-muted-foreground hover:text-foreground z-10"
                        >
                          {showPasswords.current ? <EyeOff className="w-4 h-4" /> : <Eye className="w-4 h-4" />}
                        </button>
                        <Input
                          type={showPasswords.current ? "text" : "password"}
                          name="currentPassword"
                          value={passwordData.currentPassword}
                          onChange={handlePasswordChange}
                          placeholder="Nhập mật khẩu hiện tại"
                          className="pl-10"
                        />
                      </div>
                    </div>

                    {/* New Password */}
                    <div>
                      <label className="block text-sm font-medium text-foreground mb-1">
                        Mật khẩu mới *
                      </label>
                      <div className="relative">
                        <button
                          type="button"
                          onClick={() => togglePasswordVisibility("new")}
                          className="absolute left-3 top-1/2 -translate-y-1/2 text-muted-foreground hover:text-foreground z-10"
                        >
                          {showPasswords.new ? <EyeOff className="w-4 h-4" /> : <Eye className="w-4 h-4" />}
                        </button>
                        <Input
                          type={showPasswords.new ? "text" : "password"}
                          name="newPassword"
                          value={passwordData.newPassword}
                          onChange={handlePasswordChange}
                          placeholder="Nhập mật khẩu mới"
                          className="pl-10"
                        />
                      </div>
                      <div className="text-xs text-muted-foreground mt-1">
                        Tối thiểu 6 ký tự
                      </div>
                    </div>

                    {/* Confirm Password */}
                    <div>
                      <label className="block text-sm font-medium text-foreground mb-1">
                        Xác nhận mật khẩu mới *
                      </label>
                      <div className="relative">
                        <button
                          type="button"
                          onClick={() => togglePasswordVisibility("confirm")}
                          className="absolute left-3 top-1/2 -translate-y-1/2 text-muted-foreground hover:text-foreground z-10"
                        >
                          {showPasswords.confirm ? <EyeOff className="w-4 h-4" /> : <Eye className="w-4 h-4" />}
                        </button>
                        <Input
                          type={showPasswords.confirm ? "text" : "password"}
                          name="confirmPassword"
                          value={passwordData.confirmPassword}
                          onChange={handlePasswordChange}
                          placeholder="Nhập lại mật khẩu mới"
                          className="pl-10"
                        />
                      </div>
                    </div>
                  </div>

                  <div className="flex gap-3 mt-6">
                    <Button
                      variant="outline"
                      onClick={() => {
                        setShowPasswordModal(false)
                        setPasswordError("")
                        setPasswordData({
                          currentPassword: "",
                          newPassword: "",
                          confirmPassword: "",
                        })
                      }}
                      className="flex-1"
                    >
                      Hủy
                    </Button>
                    <Button
                      onClick={handleChangePassword}
                      className="flex-1"
                    >
                      Đổi mật khẩu
                    </Button>
                  </div>
                </>
              )}
            </div>
          </div>
        )}
      </div>
    </PageLayout>
  )
}
