package org.example;




import java.util.*;


public class GarbageCollectorImplementation implements GarbageCollector {
  @Override
  public List<ApplicationBean> collect(HeapInfo heap, StackInfo stack) {
    Map<String, ApplicationBean> beans = heap.getBeans();
    Deque<StackInfo.Frame> frames = stack.getStack();
    Set<ApplicationBean> aliveBeans = getAliveBeans(frames);
    Set<ApplicationBean> aliveHeapBeans = getAliveBeansFromHeap(beans);
    ArrayList<ApplicationBean> garbageList = new ArrayList<>();
    for (ApplicationBean bean : aliveHeapBeans) {
      if (!aliveBeans.contains(bean)){
        garbageList.add(bean);
      }
    }
    return garbageList;
  }

  public Set<ApplicationBean> getAliveBeans(Deque<StackInfo.Frame> frames) {
    Set<ApplicationBean> aliveBeans = new HashSet<>();
    for (StackInfo.Frame frame :
            frames) {
      for (ApplicationBean bean :
              frame.parameters) {
        aliveBeans.addAll(getRelation(bean));
      }
    }
    return aliveBeans;
  }

  public Set<ApplicationBean> getAliveBeansFromHeap(Map<String, ApplicationBean> beans) {
    Set<ApplicationBean> aliveBeans = new HashSet<>();
    for (Map.Entry<String, ApplicationBean> bean :
            beans.entrySet()) {
      ApplicationBean frame = bean.getValue();
        aliveBeans.addAll(getRelation(frame));
    }
    System.out.println(aliveBeans);
    return aliveBeans;
  }
    private Set<ApplicationBean> getRelation(ApplicationBean bean){
      Set<ApplicationBean> garbage = new HashSet<>();
      garbage.add(bean);
      bean.getFieldValues()
              .forEach(
                      (key, value) -> {
                        garbage.addAll(getRelation(value));
                      });
      return garbage;

  }
}

