import { create } from "zustand";

const storedUsername = localStorage.getItem('username')
const storedRole = localStorage.getItem('role')
const isAuthenticated = storedUsername != null && storedRole != null

export const useAuthStore = create((set) => ({
  username: storedUsername,
  role: storedRole,
  isAuthenticated: isAuthenticated,
  onSignedIn: (username, role) => set({ username, role, isAuthenticated: true }),
  onSignedOut: () => set({ isAuthenticated: false, username: undefined, role: undefined }),
}))