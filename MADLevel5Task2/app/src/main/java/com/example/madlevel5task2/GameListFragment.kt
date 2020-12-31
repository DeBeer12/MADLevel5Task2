package com.example.madlevel5task2

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_game_list.*

class GameListFragment : Fragment() {
    private lateinit var gameAdapter: GameAdapter
    private lateinit var viewManager: RecyclerView.LayoutManager

    private var games: ArrayList<Game> = arrayListOf()
    private val viewModel: GameViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_game_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRv()
        observeAddGameResult()
    }

    private fun observeAddGameResult() {
        viewModel.games.observe(viewLifecycleOwner, Observer { games ->
            this@GameListFragment.games.clear()
            this@GameListFragment.games.addAll(games)
            this@GameListFragment.games.sortBy {
                it.releaseDate
            }
            gameAdapter.notifyDataSetChanged()

        })
    }

    private fun initRv() {
        gameAdapter = GameAdapter(games)
        viewManager = LinearLayoutManager(activity)

        createItemTouchHelper().attachToRecyclerView(rvGameBacklog)

        rvGameBacklog.apply {
            setHasFixedSize(true)
            layoutManager = viewManager
            adapter = gameAdapter
        }
    }

    private fun createItemTouchHelper(): ItemTouchHelper {
        val callback = object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                val gameToDelete = games[position]
                viewModel.deleteGame(gameToDelete)
            }
        }
        return ItemTouchHelper(callback)
    }
}