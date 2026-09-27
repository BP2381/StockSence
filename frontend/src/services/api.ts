import type { DashboardSummary } from './dashboard'

const API_BASE_URL = 'http://localhost:8080'

export async function getDashboardSummary(): Promise<DashboardSummary> {
  const token = localStorage.getItem('token')

  const response = await fetch(`${API_BASE_URL}/api/dashboard/summary`, {
    headers: {
      Authorization: `Bearer ${token}`,
    },
  })

  if (!response.ok) {
    throw new Error('Failed to fetch dashboard summary')
  }

  return response.json()
}