package com.common.util;

import java.util.ArrayList;
import java.util.List;

public class ListUtiles {

	public static boolean isEmpty(ArrayList list) {
		if (null == list || list.isEmpty()) {
			return true;
		}
		return false;
	}

	public static boolean isEmpty(List list) {
		if (null == list || list.isEmpty()) {
			return true;
		}
		return false;
	}

	public static boolean isAllEmpty(ArrayList l1, ArrayList l2) {
		if (isEmpty(l1) && isEmpty(l2)) {
			return true;
		}
		return false;
	}
	public static int getListSize(ArrayList list){
		return (null==list?0:list.size());
	}
	public static int getListSize(List list){
		return (null==list?0:list.size());
	}

}
