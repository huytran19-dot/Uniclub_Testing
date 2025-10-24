"use client"

import { useEffect, useState } from "react"
import { useNavigate } from "react-router-dom"
import Card from "../../components/Card"
import Table from "../../components/Table"
import Badge from "../../components/Badge"
import Breadcrumb from "../../components/Breadcrumb"
import { api } from "../../lib/api"
import { formatDate, formatDateTime, formatMoney, getStatusLabel, getStatusType } from "../../lib/utils"

export default function GrnList() {
  const navigate = useNavigate()
  const [grnHeaders, setGrnHeaders] = useState([])
  const [suppliers, setSuppliers] = useState([])

  useEffect(() => {
    loadData()
  }, [])

  const loadData = async () => {
    const [grnHeadersData, suppliersData] = await Promise.all([api.list("grn-headers"), api.list("suppliers")])
    setGrnHeaders(grnHeadersData || [])
    setSuppliers(suppliersData || [])
  }

  const getSupplierName = (id) => suppliers.find((s) => s.id === id)?.name || "-"

  const columns = [
    { key: "id", label: "Mã phiếu" },
    {
      key: "supplierId",
      label: "Nhà cung cấp",
      render: (row) => getSupplierName(row.supplierId),
    },
    { key: "receivedDate", label: "Ngày nhận", render: (row) => formatDateTime(row.receivedDate) },
    {
      key: "totalCost",
      label: "Tổng tiền",
      render: (row) => formatMoney(row.totalCost),
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
          {grnHeaders.length === 0 ? (
            <div className="text-center py-8 text-neutral-500">
              Chưa có phiếu nhập nào
            </div>
          ) : (
            <Table columns={columns} data={grnHeaders} onView={(row) => navigate(`/grn/${row.id}`)} />
          )}
        </div>
      </Card>
    </div>
  )
}
