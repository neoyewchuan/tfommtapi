package com.tfommtapi.models;

import java.util.ArrayList;

public class TransactionsPagedResult {

		private ArrayList<Transaction> List;
		private String Next;
		private String Prev;
		private int TotalCount;
		
		public TransactionsPagedResult()	{}

		public ArrayList<Transaction> getList() {
			return List;
		}

		public String getNext() {
			return Next;
		}

		public String getPrev() {
			return Prev;
		}

		public int getTotalCount() {
			return TotalCount;
		}

		public void setList(ArrayList<Transaction> list) {
			List = list;
		}

		public void setNext(String next) {
			Next = next;
		}

		public void setPrev(String prev) {
			Prev = prev;
		}

		public void setTotalCount(int totalCount) {
			TotalCount = totalCount;
		}
		
		
	
}
