void test(int serving_wl_type, int serving_wl_class, int cfqg, int workload_expires, int jiffies){
if ( cfqg   - >   saved_wl_slice )     {  cfqd - >  workload_expires = jiffies    +  cfqg     -  >        saved_wl_slice ;   cfqd - >  serving_wl_type = cfqg    -  >        saved_wl_type ;   cfqd - >  serving_wl_class = cfqg    -  >        saved_wl_class ;  }    }