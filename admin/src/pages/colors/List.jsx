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

export default function ColorList() {
  const navigate = useNavigate()
  const [colors, setColors] = useState([])
  const [toast, setToast] = useState(null)
  const [confirmDelete, setConfirmDelete] = useState(null)

  useEffect(() => {
    loadColors()
  }, [])

  const loadColors = async () => {
    const data = await api.list("colors")
    setColors(data || [])
  }

  const handleDelete = async () => {
    await api.delete("colors", confirmDelete.id)
    setToast({ message: "Xóa màu sắc thành công", type: "success" })
    setConfirmDelete(null)
    loadColors()
  }

  const columns = [
    { key: "id", label: "ID" },
    { key: "name", label: "Tên màu" },
    {
      key: "hex_code",
      label: "Mã màu",
      render: (row) => (
        <div className="flex items-center gap-2">
          <div className="w-6 h-6 rounded border border-neutral-300" style={{ backgroundColor: row.hex_code }} />
          <span>{row.hex_code}</span>
        </div>
      ),
    },
    {
      key: "status",
      label: "Trạng thái",
      render: (row) => <Badge status={getStatusType(row.status)} label={getStatusLabel(row.status)} />,
    },
  ]

  return (
    <div>
      <Breadcrumb items={[{ label: "Sản phẩm", path: "/products" }, { label: "Màu sắc" }]} />
      <div className="flex items-center justify-between mb-6">
        <h1 className="text-3xl font-bold">Màu sắc</h1>
        <button
          onClick={() => navigate("/colors/new")}
          className="px-4 py-2 bg-blue-600 text-white rounded-lg hover:bg-blue-700"
        >
          + Thêm mới
        </button>
      </div>

      <Card>
        <div className="p-6">
          <Table
            columns={columns}
            data={colors}
            onEdit={(row) => navigate(`/colors/${row.id}`)}
            onDelete={(row) => setConfirmDelete(row)}
          />
        </div>
      </Card>

      {confirmDelete && (
        <Confirm
          isOpen={true}
          title="Xóa màu sắc"
          message={`Bạn có chắc muốn xóa màu "${confirmDelete.name}"?`}
          onConfirm={handleDelete}
          onCancel={() => setConfirmDelete(null)}
        />
      )}

      {toast && <Toast {...toast} onClose={() => setToast(null)} />}
    </div>
  )
}
