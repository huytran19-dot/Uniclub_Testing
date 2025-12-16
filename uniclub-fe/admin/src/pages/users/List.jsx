import React, { useState, useEffect } from 'react'
import { useNavigate } from 'react-router-dom'
import { api } from '../../lib/api'
import { formatDate, getStatusLabel, getStatusType } from '../../lib/utils'
import Table from '../../components/Table'
import Toast from '../../components/Toast'
import { ActionButtonGroup } from '../../components/ActionButtons'
import Confirm from '../../components/Confirm'

export default function UserList() {
  const [users, setUsers] = useState([])
  const [roles, setRoles] = useState([])
  const [loading, setLoading] = useState(true)
  const [toast, setToast] = useState(null)
  const [confirmDialog, setConfirmDialog] = useState({ isOpen: false, user: null })
  const [actionLoading, setActionLoading] = useState(null)
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
      setActionLoading(`status-${user.id}`)
      // Status: 0 = Unverified, 1 = Active, 2 = Disabled
      // Toggle between Active (1) and Disabled (2)
      const newStatus = user.status === 1 ? 2 : 1
      
      // Gửi toàn bộ thông tin user để tránh lỗi validation
      const updatedUser = {
        email: user.email,
        fullname: user.fullname || '',
        phone: user.phone || '',
        address: user.address || '',
        roleId: user.roleId,
        status: newStatus
      }
      
      await api.update("users", user.id, updatedUser)
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
    } finally {
      setActionLoading(null)
    }
  }

  const handleDelete = (user) => {
    setConfirmDialog({ isOpen: true, user })
  }

  const confirmDelete = async () => {
    if (!confirmDialog.user) return
    
    try {
      setActionLoading(`delete-${confirmDialog.user.id}`)
      await api.delete("users", confirmDialog.user.id)
      setToast({ 
        message: "Xóa người dùng thành công", 
        type: "success" 
      })
      setConfirmDialog({ isOpen: false, user: null })
      loadData()
    } catch (error) {
      console.error("Error deleting user:", error)
      
      // Extract error message from response
      let errorMessage = "Có lỗi xảy ra khi xóa người dùng"
      
      if (error.response?.data) {
        // If backend returns a string message
        if (typeof error.response.data === 'string') {
          errorMessage = error.response.data
        } 
        // If backend returns an object with message
        else if (error.response.data.message) {
          errorMessage = error.response.data.message
        }
      } else if (error.message) {
        errorMessage = error.message
      }
      
      setToast({ 
        message: errorMessage, 
        type: "error" 
      })
      setConfirmDialog({ isOpen: false, user: null })
    } finally {
      setActionLoading(null)
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
      render: (user) => {
        const statusConfig = {
          0: { bg: 'bg-yellow-100', text: 'text-yellow-800', label: 'Chưa xác thực' },
          1: { bg: 'bg-green-100', text: 'text-green-800', label: 'Hoạt động' },
          2: { bg: 'bg-red-100', text: 'text-red-800', label: 'Vô hiệu hóa' }
        }
        const config = statusConfig[user.status] || statusConfig[2]
        
        return (
          <span className={`inline-flex items-center px-2.5 py-0.5 rounded-full text-xs font-medium ${config.bg} ${config.text}`}>
            {config.label}
          </span>
        )
      }
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
          <ActionButtonGroup
            row={user}
            onEdit={(user) => navigate(`/users/${user.id}`)}
            onDelete={handleDelete}
            editLabel="Sửa"
            deleteLabel="Xóa"
            loading={actionLoading === `delete-${user.id}` ? 'delete' : null}
          />
          <button
            onClick={() => handleToggleStatus(user)}
            disabled={actionLoading === `status-${user.id}` || user.status === 0}
            className={`inline-flex items-center px-3 py-1.5 rounded-md text-xs font-medium transition-colors focus-visible:outline-none focus-visible:ring-2 focus-visible:ring-offset-2 disabled:opacity-50 disabled:cursor-not-allowed ${
              user.status === 1 
                ? 'bg-red-50 text-red-700 hover:bg-red-100 border border-red-200 focus-visible:ring-red-500' 
                : 'bg-green-50 text-green-700 hover:bg-green-100 border border-green-200 focus-visible:ring-green-500'
            }`}
            title={user.status === 0 ? 'Không thể thay đổi trạng thái tài khoản chưa xác thực' : ''}
          >
            {actionLoading === `status-${user.id}` ? (
              <span className="flex items-center gap-1.5">
                <svg className="animate-spin h-3.5 w-3.5" xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24">
                  <circle className="opacity-25" cx="12" cy="12" r="10" stroke="currentColor" strokeWidth="4"></circle>
                  <path className="opacity-75" fill="currentColor" d="M4 12a8 8 0 018-8V0C5.373 0 0 5.373 0 12h4zm2 5.291A7.962 7.962 0 014 12H0c0 3.042 1.135 5.824 3 7.938l3-2.647z"></path>
                </svg>
                Đang xử lý...
              </span>
            ) : (
              user.status === 1 ? 'Vô hiệu hóa' : 'Kích hoạt'
            )}
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

      <Confirm
        isOpen={confirmDialog.isOpen}
        title="Xác nhận xóa người dùng"
        message={`Bạn có chắc chắn muốn xóa người dùng "${confirmDialog.user?.email}"? Hành động này không thể hoàn tác.`}
        confirmText="Xóa người dùng"
        cancelText="Hủy bỏ"
        variant="danger"
        onConfirm={confirmDelete}
        onCancel={() => setConfirmDialog({ isOpen: false, user: null })}
      />
    </div>
  )
}
