import { createApi, fetchBaseQuery } from '@reduxjs/toolkit/query/react'
import type { User } from '../api/types'

type SignupRequest = {
  name: string
  email: string
  password: string
}

type LoginRequest = {
  email: string
  password: string
}

export const api = createApi({
  reducerPath: 'api',
  baseQuery: fetchBaseQuery({
    baseUrl: '/',
    credentials: 'include',
  }),
  tagTypes: ['Session', 'Profile'],
  endpoints: (builder) => ({
    getSession: builder.query<User, void>({
      query: () => '/api/auth/session',
      providesTags: ['Session'],
    }),
    getProfile: builder.query<User, void>({
      query: () => '/api/v1/users/me',
      providesTags: ['Profile'],
    }),
    signup: builder.mutation<User, SignupRequest>({
      query: (body) => ({
        url: '/api/auth/signup',
        method: 'POST',
        body,
      }),
    }),
    login: builder.mutation<{ authenticated: boolean }, LoginRequest>({
      query: ({ email, password }) => ({
        url: '/api/auth/login',
        method: 'POST',
        headers: {
          'Content-Type': 'application/x-www-form-urlencoded',
        },
        body: new URLSearchParams({
          username: email,
          password,
        }),
      }),
      invalidatesTags: ['Session', 'Profile'],
    }),
    logout: builder.mutation<{ authenticated: boolean }, void>({
      query: () => ({
        url: '/api/auth/logout',
        method: 'POST',
      }),
      invalidatesTags: ['Session', 'Profile'],
    }),
  }),
})

export const {
  useGetSessionQuery,
  useGetProfileQuery,
  useSignupMutation,
  useLoginMutation,
  useLogoutMutation,
} = api
