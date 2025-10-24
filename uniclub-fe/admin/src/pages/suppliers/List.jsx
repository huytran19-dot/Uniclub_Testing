"use client"

import { useEffect, useState } from "react"
import { useNavigate } from "react-router-dom"
import Card from "../../components/Card"
import Table from "../../components/Table"
import Badge from "../../components/Badge"
import Toast from "../../components/Toast"
import Confirm from "../../components/Confirm"
import Breadcrumb from "../../components/Breadcrumb"
import { api } from "../../lib/api"
import { getStatusLabel, getStatusType } from "../../lib/utils"

export default function SupplierList() {
  const navigate = useNavigate()
  const [suppliers, setSuppliers] = useState([])
  const [toast, setToast] = useState(null)
  const [confirmDelete, setConfirmDelete] = useState(null)

  useEffect(() => {
    loadSuppliers()
  }, [])

  const loadSuppliers = async () => {
    const data = await api.list("suppliers")
    setSuppliers(data || [])
  }

  const handleDelete = async () => {
    await api.delete("suppliers", confirmDelete.id)
    setToast({ message: "Xóa nhà cung cấp thành công", type: "success" })
    setConfirmDelete(null)
    loadSuppliers()
  }

  const columns = [
    { key: "id", label: "ID" },
    { key: "name", label: "Tên NCC" },
    { key: "contactPerson", label: "Người liên hệ" },
    { key: "phone", label: "SĐT" },
    { key: "email", label: "Email" },
    { key: "address", label: "Địa chỉ" },
    {
      key: "status",
      label: "Trạng thái",
      render: (row) => <Badge status={getStatusType(row.status)} label={getStatusLabel(row.status)} />,
    },
  ]

  return (
    <div>
      <Breadcrumb items={[{ label: "Nhà cung cấp" }]} />
      <div className="flex items-center justify-between mb-6">
        <h1 className="text-3xl font-bold">Nhà cung cấp</h1>
        <button
          onClick={() => navigate("/suppliers/new")}
          className="px-4 py-2 bg-blue-600 text-white rounded-lg hover:bg-blue-700"
        >
          + Thêm mới
        </button>
      </div>

      <Card>
        <div className="p-6">
          <Table
            columns={columns}
            data={suppliers}
            onEdit={(row) => navigate(`/suppliers/${row.id}`)}
            onDelete={(row) => setConfirmDelete(row)}
          />
        </div>
      </Card>

      {confirmDelete && (
        <Confirm
          isOpen={true}
          title="Xóa nhà cung cấp"
          message={`Bạn có chắc muốn xóa nhà cung cấp "${confirmDelete.name}"?`}
          onConfirm={handleDelete}
          onCancel={() => setConfirmDelete(null)}
        />
      )}

      {toast && <Toast {...toast} onClose={() => setToast(null)} />}
    </div>
  )
}
