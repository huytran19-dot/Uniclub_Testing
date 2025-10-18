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
import { formatDate, getStatusLabel, getStatusType } from "../../lib/utils"

export default function BrandList() {
  const navigate = useNavigate()
  const [brands, setBrands] = useState([])
  const [toast, setToast] = useState(null)
  const [confirmDelete, setConfirmDelete] = useState(null)

  useEffect(() => {
    loadBrands()
  }, [])

  const loadBrands = async () => {
    const data = await api.list("brands")
    setBrands(data || [])
  }

  const handleDelete = async () => {
    await api.delete("brands", confirmDelete.id)
    setToast({ message: "Xóa nhãn hàng thành công", type: "success" })
    setConfirmDelete(null)
    loadBrands()
  }

  const columns = [
    { key: "id", label: "ID" },
    { key: "name", label: "Tên nhãn hàng" },
    {
      key: "status",
      label: "Trạng thái",
      render: (row) => <Badge status={getStatusType(row.status)} label={getStatusLabel(row.status)} />,
    },
    { key: "created_at", label: "Ngày tạo", render: (row) => formatDate(row.created_at) },
  ]

  return (
    <div>
      <Breadcrumb items={[{ label: "Sản phẩm", path: "/products" }, { label: "Nhãn hàng" }]} />
      <div className="flex items-center justify-between mb-6">
        <h1 className="text-3xl font-bold">Nhãn hàng</h1>
        <button
          onClick={() => navigate("/brands/new")}
          className="px-4 py-2 bg-blue-600 text-white rounded-lg hover:bg-blue-700"
        >
          + Thêm mới
        </button>
      </div>

      <Card>
        <div className="p-6">
          <Table
            columns={columns}
            data={brands}
            onEdit={(row) => navigate(`/brands/${row.id}`)}
            onDelete={(row) => setConfirmDelete(row)}
          />
        </div>
      </Card>

      {confirmDelete && (
        <Confirm
          isOpen={true}
          title="Xóa nhãn hàng"
          message={`Bạn có chắc muốn xóa nhãn hàng "${confirmDelete.name}"?`}
          onConfirm={handleDelete}
          onCancel={() => setConfirmDelete(null)}
        />
      )}

      {toast && <Toast {...toast} onClose={() => setToast(null)} />}
    </div>
  )
}
