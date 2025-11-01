import React, { useState, useEffect } from 'react'
import { useNavigate } from 'react-router-dom'
import { api } from '../../lib/api'
import { formatDate, getStatusLabel, getStatusType } from '../../lib/utils'
import Table from '../../components/Table'
import Toast from '../../components/Toast'

export default function UserList() {
  const [users, setUsers] = useState([])
  const [roles, setRoles] = useState([])
  const [loading, setLoading] = useState(true)
  const [toast, setToast] = useState(null)
  const navigate = useNavigate()

  const loadData = async () => {
    try {
      const [usersData, rolesData] = await Promise.all([
        api.list("users"),
        api.list("roles")
      ])
      setUsers(usersData || [])
      setRoles(rolesData || [])
    } catch (error) {
      console.error("Error loading users:", error)
      setToast({ message: "Không thể tải danh sách người dùng", type: "error" })
    } finally {
      setLoading(false)
    }
  }

  useEffect(() => {
    loadData()
  }, [])

  const getRoleName = (roleId) => {
    const role = roles.find(r => r.id === roleId)
    return role ? role.name : "N/A"
  }

  const handleToggleStatus = async (user) => {
    try {
      const newStatus = user.status === 1 ? 0 : 1
      await api.update("users", user.id, { status: newStatus })
      setToast({ 
        message: `${newStatus === 1 ? 'Kích hoạt' : 'Vô hiệu hóa'} tài khoản thành công`, 
        type: "success" 
      })
      loadData()
    } catch (error) {
      console.error("Error updating user status:", error)
      setToast({ 
        message: error.message || "Có lỗi xảy ra khi cập nhật trạng thái", 
        type: "error" 
      })
    }
  }

  const columns = [
    {
      key: 'id',
      label: 'ID',
      render: (user) => user.id
    },
    {
      key: 'email',
      label: 'Email',
      render: (user) => (
        <div className="font-medium text-neutral-900">
          {user.email}
        </div>
      )
    },
    {
      key: 'fullname',
      label: 'Họ tên',
      render: (user) => user.fullname || "N/A"
    },
    {
      key: 'role',
      label: 'Vai trò',
      render: (user) => (
        <span className="inline-flex items-center px-2.5 py-0.5 rounded-full text-xs font-medium bg-blue-100 text-blue-800">
          {getRoleName(user.roleId)}
        </span>
      )
    },
    {
      key: 'status',
      label: 'Trạng thái',
      render: (user) => (
        <span className={`inline-flex items-center px-2.5 py-0.5 rounded-full text-xs font-medium ${
          user.status === 1 
            ? 'bg-green-100 text-green-800' 
            : 'bg-red-100 text-red-800'
        }`}>
          {getStatusLabel(user.status)}
        </span>
      )
    },
    {
      key: 'createdAt',
      label: 'Ngày tạo',
      render: (user) => formatDate(user.createdAt)
    },
    {
      key: 'actions',
      label: 'Thao tác',
      render: (user) => (
        <div className="flex items-center gap-2">
          <button
            onClick={() => navigate(`/users/${user.id}`)}
            className="text-blue-600 hover:text-blue-800 text-sm font-medium"
          >
            Sửa
          </button>
          <button
            onClick={() => handleToggleStatus(user)}
            className={`text-sm font-medium ${
              user.status === 1 
                ? 'text-red-600 hover:text-red-800' 
                : 'text-green-600 hover:text-green-800'
            }`}
          >
            {user.status === 1 ? 'Vô hiệu hóa' : 'Kích hoạt'}
          </button>
        </div>
      )
    }
  ]

  if (loading) {
    return (
      <div className="flex items-center justify-center h-64">
        <div className="w-8 h-8 border-4 border-blue-500 border-t-transparent rounded-full animate-spin"></div>
      </div>
    )
  }

  return (
    <div className="space-y-6">
      <div className="flex justify-between items-center">
        <div>
          <h1 className="text-2xl font-bold text-neutral-900">Quản lý người dùng</h1>
          <p className="text-neutral-600">Quản lý tài khoản người dùng trong hệ thống</p>
        </div>
        <button
          onClick={() => navigate('/users/new')}
          className="px-4 py-2 bg-blue-600 text-white rounded-lg hover:bg-blue-700 transition-colors"
        >
          Thêm người dùng
        </button>
      </div>

      <div className="bg-white rounded-lg shadow">
        <Table 
          columns={columns} 
          data={users}
          emptyMessage="Chưa có người dùng nào"
        />
      </div>

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
