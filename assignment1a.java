
public class Assignment1a {
    int min_weights = Integer.MAX_VALUE; 
    List<Integer> best_order = new ArrayList<Integer>();
    public static void  main() {

        int[][] weights = new int[8][8];
        int count = 1 ;
        
        
        for(int i = 0; i < 8; i++){
            for(int j = 0; j < 8; j ++){
                weights[i][j] = count ;
                count +=1 ;
            }
        }
        
        Set<String> forbidden = new HashSet<String>();
        Set<String> forced_partial_assignments = new HashSet<String>();
        forbidden.add("1,1");
        forced_partial_assignments.add("3,4");
        
        List<Integer> machinesLeft = new List<Integer>();
        for(int i = 0; i < 8; i ++){
            machinesLeft.add(i+1);
        }
        
        findBestCombo(weights, new HashSet(machinesLeft), new HashSet<String>(tasksLeft), 0, new ArrayList<Integer>(), forbidden, forced_partial_assignments);
        System.out.println("solution found:");
        System.out.println(best_order);             
        
    }


    // once the partial assignments are complete, find best weight with remaining machines and tasks 
    void findBestCombo(int[][] weights, List<Integer> machinesLeft, HashSet<Integer> tasksLeft, weight_so_far, List<Integer> curr_order, HashSet<String> forbidden, Set<String> forced_partial_assignments){
        if(weight_so_far >= min_weight){
            return ;
        }
        if(machinesLeft.size() == 0 && tasksLeft.size() == 0){
            if(weight_so_far < min_weights && forced_partial_assignments.size() == 0){
                best_order = new ArrayList<>(curr_order);
            }
            return ;
        }

            

        int machine = machinesLeft.remove(0);

        // first recursively only carry 

        // try all other possible assignments 
        for(Integer task: tasks_left){

            String current_assignment = "" + machine + "," + task;

            // if the current assignment is forbidden, do not perform this assignment 
            if( !forbidden.contains(curr_assignment)){
                tasks_left.remove(task);
                curr_order.add(task);
                bool forced_partial_seen = false; 
                if(forced_partial_assignments.contains(i,j)){
                    forced_partial_seen = true; 
                    forced_partial_assignments.remove(current_assignment);
                }
                findBestCombo(weights, machinesLeft, tasksLeft, weights[machine][task] + weight_so_far, curr_order);

                if(forced_partial_seen == true){
                    forced_partial_assignments.add(current_assignment);
                }

                curr_order.remove(task);
                tasks_left.add(task);

            }



        }
    }
}