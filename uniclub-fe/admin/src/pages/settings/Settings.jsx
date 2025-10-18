import Card from "../../components/Card"
import Breadcrumb from "../../components/Breadcrumb"

export default function Settings() {
  return (
    <div>
      <Breadcrumb items={[{ label: "Cài đặt" }]} />
      <h1 className="text-3xl font-bold mb-6">Cài đặt</h1>

      <div className="grid grid-cols-1 md:grid-cols-2 gap-6">
        <Card className="p-6">
          <h2 className="text-lg font-semibold mb-4">Cài đặt chung</h2>
          <div className="space-y-4">
            <div>
              <label className="block text-sm font-medium text-neutral-700 mb-2">Tên cửa hàng</label>
              <input
                type="text"
                defaultValue="UniClub"
                className="w-full px-3 py-2 border border-neutral-200 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500"
              />
            </div>
            <div>
              <label className="block text-sm font-medium text-neutral-700 mb-2">Email liên hệ</label>
              <input
                type="email"
                defaultValue="contact@uniclub.com"
                className="w-full px-3 py-2 border border-neutral-200 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500"
              />
            </div>
            <button className="w-full px-4 py-2 bg-blue-600 text-white rounded-lg hover:bg-blue-700">
              Lưu thay đổi
            </button>
          </div>
        </Card>

        <Card className="p-6">
          <h2 className="text-lg font-semibold mb-4">Thông tin API</h2>
          <div className="space-y-4">
            <div>
              <label className="block text-sm font-medium text-neutral-700 mb-2">Base URL</label>
              <input
                type="text"
                defaultValue={import.meta.env.VITE_API_URL || "http://localhost:8080/api"}
                readOnly
                className="w-full px-3 py-2 border border-neutral-200 rounded-lg bg-neutral-50"
              />
            </div>
            <p className="text-sm text-neutral-600">Cấu hình API URL trong biến môi trường VITE_API_URL</p>
          </div>
        </Card>
      </div>
    </div>
  )
}
