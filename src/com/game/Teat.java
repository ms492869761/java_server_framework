package com.game;

import java.util.Random;

public class Teat {
	
	public static void main(String[] args) {
		
		for(int i=0;i<100;i++) {
			System.out.println(getRandomRank(2, 100, 10));
		}
		
		
	}
	
	
	
	public static int getRandomRank(int myRank,int size,int getCount) {
		Random random=new Random();
		if(size<getCount+1) {
			int nextInt = random.nextInt(size-1)+1;
			if(nextInt<myRank) {
				return nextInt;
			} else {
				return nextInt+1;
			}
		}
		int beforeCount=myRank-1;
		int afterCount=size-myRank;
		int nextInt = random.nextInt(getCount)+1;
		if(beforeCount<getCount/2) {
			if(nextInt<myRank) {
				return nextInt;
			} else {
				return nextInt+1;
			}
		}
		if(afterCount<getCount/2) {
			int rank= size-nextInt+1;
			if(rank<=myRank) {
				return rank-1;
			} else {
				return rank;
			}
		}
		int a=getCount/2;
		if(nextInt<=a) {
			return myRank+nextInt-a-1;
		} else {
			return myRank+nextInt-a;
		}
	}
	
}
