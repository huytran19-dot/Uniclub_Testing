import React, { useState, useEffect } from 'react'
import { useNavigate, useParams } from 'react-router-dom'
import { api } from '../../lib/api'
import FormField from '../../components/FormField'
import Toast from '../../components/Toast'

export default function UserForm() {
  const { id } = useParams()
  const navigate = useNavigate()
  const [loading, setLoading] = useState(false)
  const [toast, setToast] = useState(null)
  const [roles, setRoles] = useState([])
  
  const [form, setForm] = useState({
    email: '',
    password: '',
    fullName: '',
    roleId: '',
    status: 1
  })

  const [errors, setErrors] = useState({})

  const loadData = async () => {
    try {
      const rolesData = await api.list("roles")
      setRoles(rolesData || [])
      
      if (id) {
        const userData = await api.get("users", id)
        setForm({
          email: userData.email || '',
          password: '', // Don't load password
          fullName: userData.fullName || '',
          roleId: userData.roleId || '',
          status: userData.status !== undefined ? userData.status : 1
        })
      }
    } catch (error) {
      console.error("Error loading data:", error)
      setToast({ message: "Không thể tải dữ liệu", type: "error" })
    }
  }

  useEffect(() => {
    loadData()
  }, [id])

  const handleChange = (field, value) => {
    setForm(prev => ({ ...prev, [field]: value }))
    // Clear error when user starts typing
    if (errors[field]) {
      setErrors(prev => ({ ...prev, [field]: '' }))
    }
  }

  const validate = () => {
    const newErrors = {}

    if (!form.email.trim()) {
      newErrors.email = 'Email không được để trống'
    } else if (!/\S+@\S+\.\S+/.test(form.email)) {
      newErrors.email = 'Email không hợp lệ'
    }

    if (!id && !form.password.trim()) {
      newErrors.password = 'Mật khẩu không được để trống'
    } else if (form.password && form.password.length < 6) {
      newErrors.password = 'Mật khẩu phải có ít nhất 6 ký tự'
    }

    if (!form.fullName.trim()) {
      newErrors.fullName = 'Họ tên không được để trống'
    }

    if (!form.roleId) {
      newErrors.roleId = 'Vai trò không được để trống'
    }

    return newErrors
  }

  const handleSubmit = async (e) => {
    e.preventDefault()
    const newErrors = validate()
    
    if (Object.keys(newErrors).length > 0) {
      setErrors(newErrors)
      return
    }

    setLoading(true)

    try {
      const payload = {
        email: form.email,
        fullName: form.fullName,
        roleId: parseInt(form.roleId),
        status: form.status
      }

      // Only include password if it's provided (for new users or password change)
      if (form.password) {
        payload.password = form.password
      }

      if (id) {
        await api.update("users", id, payload)
        setToast({ message: "Cập nhật người dùng thành công", type: "success" })
      } else {
        await api.create("users", payload)
        setToast({ message: "Tạo người dùng thành công", type: "success" })
      }

      // Delay navigation to show toast
      setTimeout(() => {
        navigate("/users")
      }, 1500)

    } catch (error) {
      console.error("Error saving user:", error)
      setToast({
        message: error.message || "Có lỗi xảy ra khi lưu người dùng",
        type: "error"
      })
    } finally {
      setLoading(false)
    }
  }

  return (
    <div className="max-w-2xl mx-auto space-y-6">
      <div>
        <h1 className="text-2xl font-bold text-neutral-900">
          {id ? 'Chỉnh sửa người dùng' : 'Thêm người dùng mới'}
        </h1>
        <p className="text-neutral-600">
          {id ? 'Cập nhật thông tin người dùng' : 'Tạo tài khoản người dùng mới'}
        </p>
      </div>

      <form onSubmit={handleSubmit} className="bg-white rounded-lg shadow p-6 space-y-6">
        <FormField
          label="Email"
          type="email"
          value={form.email}
          onChange={(value) => handleChange('email', value)}
          error={errors.email}
          required
        />

        <FormField
          label={id ? "Mật khẩu mới (để trống nếu không thay đổi)" : "Mật khẩu"}
          type="password"
          value={form.password}
          onChange={(value) => handleChange('password', value)}
          error={errors.password}
          required={!id}
        />

        <FormField
          label="Họ tên"
          type="text"
          value={form.fullName}
          onChange={(value) => handleChange('fullName', value)}
          error={errors.fullName}
          required
        />

        <div>
          <label className="block text-sm font-medium text-neutral-700 mb-2">
            Vai trò <span className="text-red-500">*</span>
          </label>
          <select
            value={form.roleId}
            onChange={(e) => handleChange('roleId', e.target.value)}
            className={`w-full px-3 py-2 border rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500 ${
              errors.roleId ? 'border-red-500' : 'border-neutral-300'
            }`}
          >
            <option value="">Chọn vai trò</option>
            {roles.map(role => (
              <option key={role.id} value={role.id}>
                {role.name} - {role.description}
              </option>
            ))}
          </select>
          {errors.roleId && (
            <p className="text-red-500 text-sm mt-1">{errors.roleId}</p>
          )}
        </div>

        <div>
          <label className="block text-sm font-medium text-neutral-700 mb-2">
            Trạng thái
          </label>
          <select
            value={form.status}
            onChange={(e) => handleChange('status', parseInt(e.target.value))}
            className="w-full px-3 py-2 border border-neutral-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500"
          >
            <option value={1}>Hoạt động</option>
            <option value={0}>Vô hiệu hóa</option>
          </select>
        </div>

        <div className="flex gap-4 pt-4">
          <button
            type="submit"
            disabled={loading}
            className="flex-1 px-4 py-2 bg-blue-600 text-white rounded-lg hover:bg-blue-700 disabled:opacity-50 disabled:cursor-not-allowed"
          >
            {loading ? 'Đang lưu...' : (id ? 'Cập nhật' : 'Tạo mới')}
          </button>
          <button
            type="button"
            onClick={() => navigate('/users')}
            className="flex-1 px-4 py-2 border border-neutral-300 text-neutral-700 rounded-lg hover:bg-neutral-50"
          >
            Hủy
          </button>
        </div>
      </form>

      {toast && (
        <Toast
          message={toast.message}
          type={toast.type}
          onClose={() => setToast(null)}
        />
      )}
    </div>
  )
}
