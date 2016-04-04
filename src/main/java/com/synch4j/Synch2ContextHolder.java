package com.synch4j;

/**
 * 
 * @author XieGuanNan
 * @date 2015-8-31-下午2:47:10
 * 和1.0一样的结构，切换分区用的
 * 
 * @author XieGuanNan
 * @date 2015-8-31-下午2:47:10
 * 会导致有commons包依赖synch2包，修改了，待删除
 * 
 * @author XieGuanNan
 * @date 2015-11-9-下午18:00:00
 * 暂时不删除了，没什么用处，但是未来如果要分出去独立的包，还是有点用处，留着吧
 */
public class Synch2ContextHolder {
	
  private static ThreadLocal<Synch2Context> synchContext2Holder = new ThreadLocal<Synch2Context>();

  public static void setSynchContext(Synch2Context synchContext) {
	  synchContext2Holder.set(synchContext);
  }

  public static Synch2Context getSynchContext() {
    return synchContext2Holder.get();
  }
  
}