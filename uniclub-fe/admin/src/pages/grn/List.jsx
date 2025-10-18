"use client"

import { useEffect, useState } from "react"
import { useNavigate } from "react-router-dom"
import Card from "../../components/Card"
import Table from "../../components/Table"
import Badge from "../../components/Badge"
import Breadcrumb from "../../components/Breadcrumb"
import { api } from "../../lib/api"
import { formatDate, formatMoney, getStatusLabel, getStatusType } from "../../lib/utils"

export default function GrnList() {
  const navigate = useNavigate()
  const [grns, setGrns] = useState([])
  const [suppliers, setSuppliers] = useState([])

  useEffect(() => {
    loadData()
  }, [])

  const loadData = async () => {
    const [grnData, suppData] = await Promise.all([api.list("grn"), api.list("suppliers")])
    setGrns(grnData || [])
    setSuppliers(suppData || [])
  }

  const getSupplierName = (id) => suppliers.find((s) => s.id === id)?.name || "-"

  const columns = [
    { key: "id", label: "Mã phiếu" },
    {
      key: "id_supplier",
      label: "Nhà cung cấp",
      render: (row) => getSupplierName(row.id_supplier),
    },
    { key: "received_date", label: "Ngày nhận", render: (row) => formatDate(row.received_date) },
    {
      key: "total_cost",
      label: "Tổng tiền",
      render: (row) => formatMoney(row.total_cost),
    },
    {
      key: "status",
      label: "Trạng thái",
      render: (row) => <Badge status={getStatusType(row.status)} label={getStatusLabel(row.status)} />,
    },
  ]

  return (
    <div>
      <Breadcrumb items={[{ label: "Phiếu nhập" }]} />
      <div className="flex items-center justify-between mb-6">
        <h1 className="text-3xl font-bold">Phiếu nhập (GRN)</h1>
        <button
          onClick={() => navigate("/grn/new")}
          className="px-4 py-2 bg-blue-600 text-white rounded-lg hover:bg-blue-700"
        >
          + Tạo phiếu mới
        </button>
      </div>

      <Card>
        <div className="p-6">
          <Table columns={columns} data={grns} onView={(row) => navigate(`/grn/${row.id}`)} />
        </div>
      </Card>
    </div>
  )
}
