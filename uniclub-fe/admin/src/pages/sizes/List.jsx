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

export default function SizeList() {
  const navigate = useNavigate()
  const [sizes, setSizes] = useState([])
  const [toast, setToast] = useState(null)
  const [confirmDelete, setConfirmDelete] = useState(null)

  useEffect(() => {
    loadSizes()
  }, [])

  const loadSizes = async () => {
    const data = await api.list("sizes")
    setSizes(data || [])
  }

  const handleDelete = async () => {
    await api.delete("sizes", confirmDelete.id)
    setToast({ message: "Xóa kích cỡ thành công", type: "success" })
    setConfirmDelete(null)
    loadSizes()
  }

  const columns = [
    { key: "id", label: "ID" },
    { key: "name", label: "Kích cỡ" },
    {
      key: "status",
      label: "Trạng thái",
      render: (row) => <Badge status={getStatusType(row.status)} label={getStatusLabel(row.status)} />,
    },
  ]

  return (
    <div>
      <Breadcrumb items={[{ label: "Sản phẩm", path: "/products" }, { label: "Kích cỡ" }]} />
      <div className="flex items-center justify-between mb-6">
        <h1 className="text-3xl font-bold">Kích cỡ</h1>
        <button
          onClick={() => navigate("/sizes/new")}
          className="px-4 py-2 bg-blue-600 text-white rounded-lg hover:bg-blue-700"
        >
          + Thêm mới
        </button>
      </div>

      <Card>
        <div className="p-6">
          <Table
            columns={columns}
            data={sizes}
            onEdit={(row) => navigate(`/sizes/${row.id}`)}
            onDelete={(row) => setConfirmDelete(row)}
          />
        </div>
      </Card>

      {confirmDelete && (
        <Confirm
          isOpen={true}
          title="Xóa kích cỡ"
          message={`Bạn có chắc muốn xóa kích cỡ "${confirmDelete.name}"?`}
          onConfirm={handleDelete}
          onCancel={() => setConfirmDelete(null)}
        />
      )}

      {toast && <Toast {...toast} onClose={() => setToast(null)} />}
    </div>
  )
}
