package com.bumbing.mapper;

import org.apache.ibatis.annotations.Select;

public interface TimeMapper {

		@Select("Select now() from dual")
		public String getTime();
		
		public String getTime2();
}
