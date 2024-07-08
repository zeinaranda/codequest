package com.dicoding.testcodequest.ui.ViewPager

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dicoding.testcodequest.R
import com.dicoding.testcodequest.data.preference.AuthPreference
import com.dicoding.testcodequest.data.response.Avatar
import com.dicoding.testcodequest.data.response.Friend
import com.dicoding.testcodequest.data.response.User
import com.dicoding.testcodequest.data.retrofit.ApiConfig
import com.dicoding.testcodequest.ui.Adapter.FriendAdapter
import com.dicoding.testcodequest.ui.Adapter.LeaderboardAdapter
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ListFriendFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var friendAdapter: FriendAdapter
    private lateinit var authPreference: AuthPreference
    private val list = ArrayList<Friend>()
    private var userId: Int = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_list_friend, container, false)
        Log.d("Fragment", "Ini adalah List Friend Fragment")
        authPreference = AuthPreference(requireContext())
        userId = authPreference.getId() ?: 0

        recyclerView = view.findViewById(R.id.recyclerViewFriends)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        friendAdapter = FriendAdapter(list)
        recyclerView.adapter = friendAdapter // Menggunakan friendAdapter yang sudah diinisialisasi

        // Ambil data teman dari API menggunakan Retrofit
        fetchFriends()

        return view
    }

    private fun fetchFriends() {
        val service = ApiConfig.getApiService()
        val call = service.getFriends(userId)

        call.enqueue(object : Callback<ArrayList<Friend>> {
            override fun onResponse(call: Call<ArrayList<Friend>>, response: Response<ArrayList<Friend>>) {
                if (response.isSuccessful) {
                    val users = response.body() ?: emptyList()
                    // Konversi List<User> ke ArrayList<User>
                    val userList = ArrayList(users)

                    // Update data di dalam adapter menggunakan setList
                    friendAdapter.setList(userList)
                } else {
                    // Tampilkan pesan error jika gagal mendapatkan data
                    Log.e("Fetch Friends", "Failed to fetch friends: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<ArrayList<Friend>>, t: Throwable) {
                // Tampilkan pesan error jika gagal koneksi ke server
                Log.e("Fetch Friends", "Error fetching friends", t)
            }
        })
    }
}
