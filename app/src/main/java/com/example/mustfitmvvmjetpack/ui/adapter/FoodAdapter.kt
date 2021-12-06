package com.example.mustfitmvvmjetpack.ui.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.example.mustfitmvvmjetpack.databinding.FoodItemBinding
import com.example.mustfitmvvmjetpack.model.FoodApiModel
import com.example.mustfitmvvmjetpack.ui.view.DailyMenu
import com.example.mustfitmvvmjetpack.ui.view.FoodDetails
import com.squareup.picasso.Picasso

const val PICTURE = "Picture"
const val CALORIE = "TotalCalories"
const val NAME = "FoodName"
const val COUNT = "Count"

class FoodAdapter(private val foodsList: ArrayList<FoodApiModel>) :
    RecyclerView.Adapter<FoodAdapter.FoodHolder>() {

    class FoodHolder(val binding: FoodItemBinding) : RecyclerView.ViewHolder(binding.root) {
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FoodHolder {
        val view = FoodItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return FoodHolder(view)
    }

    override fun onBindViewHolder(holder: FoodHolder, position: Int) {

        var totalCalories = 0
        var addCount = 0
        val currentItem = foodsList[position]

        holder.binding.tvFoodLabel.text =
            foodsList[position].hints.firstOrNull()?.food?.label.toString()
        holder.binding.tvFoodCalories.text =
            foodsList[position].hints.firstOrNull()?.food?.nutrients?.ENERC_KCAL.toString()
        holder.binding.tvFoodCalories.text =
            foodsList[position].hints.firstOrNull()?.food?.nutrients?.ENERC_KCAL.toString()
        Picasso.get().load(foodsList[position].hints.firstOrNull()?.food?.image)
            .into(holder.binding.ivFood)

        holder.binding.ivAdd.setOnClickListener {
            addCount++
            holder.binding.tvCount.text = "$addCount"
            holder.binding.addToDailyFood.isVisible = true
            holder.binding.ivRemove.isVisible = true
        }

        holder.binding.ivRemove.setOnClickListener {
            addCount--
            if (addCount <= 0) {
                addCount = 0
                holder.binding.tvCount.text = "$addCount"
            } else {
                holder.binding.tvCount.text = "$addCount"
            }
        }

        holder.binding.addToDailyFood.setOnClickListener {
            if (holder.binding.tvCount.toString().isNotEmpty() && addCount != 0) {
                totalCalories += addCount * (foodsList.get(position).hints.firstOrNull()?.food?.nutrients?.ENERC_KCAL
                    ?: 1)
                Toast.makeText(
                    holder.binding.addToDailyFood.context,
                    "Total Calories : $totalCalories",
                    Toast.LENGTH_LONG
                ).show()
            } else {
                //ERROR
            }
        }

        holder.binding.btnMenu.setOnClickListener {
            val intent = Intent(holder.itemView.context, DailyMenu::class.java)
            intent.putExtra(PICTURE, currentItem.hints.firstOrNull()?.food?.image)
            intent.putExtra(CALORIE, totalCalories.toString())
            intent.putExtra(NAME, currentItem.hints.firstOrNull()?.food?.label)
            intent.putExtra(COUNT, addCount.toString())
            holder.itemView.context?.startActivity(intent)
        }

        holder.itemView.setOnClickListener {
            val intent = Intent(holder.itemView.context, FoodDetails::class.java)
            intent.apply {
                putExtra("Label", currentItem.hints.firstOrNull()?.food?.label)
                putExtra("Category", currentItem.hints.firstOrNull()?.food?.category)
                putExtra("SmallIcon", currentItem.hints.firstOrNull()?.food?.image)
                putExtra("Picture", currentItem.hints.firstOrNull()?.food?.image)
                putExtra(
                    "Calorie",
                    currentItem.hints.firstOrNull()?.food?.nutrients?.ENERC_KCAL.toString()
                )
                putExtra(
                    "Protein",
                    currentItem.hints.firstOrNull()?.food?.nutrients?.PROCNT.toString()
                )
                putExtra(
                    "Carbohydrate",
                    currentItem.hints.firstOrNull()?.food?.nutrients?.CHOCDF.toString()
                )
                putExtra("Fat", currentItem.hints.firstOrNull()?.food?.nutrients?.FAT.toString())
                holder.itemView.context.startActivity(intent)
            }
        }
    }

    override fun getItemCount(): Int {
        return foodsList.size
    }

    fun updateFoodWhenRefresh(newFoodList: List<FoodApiModel>) {
        foodsList.clear()
        foodsList.addAll(newFoodList)
        notifyDataSetChanged()//refresh to adapter
    }

}

