import java.util.*;

public class Main {

    public static int counter = 0;
    public static ArrayList<ArrayList<Integer>> notUniquePermutations = new ArrayList<>();

    public static void permute(ArrayList<Integer> arr, int k) {
        for(int i = k; i < arr.size(); ++i) {
            Collections.swap(arr, i, k);
            permute(arr, k + 1);
            Collections.swap(arr, i, k);
        }
        if(k == arr.size() - 1) {
            permutations.add(new ArrayList<>(arr));
            //System.out.println(Arrays.toString(arr.toArray()));
            //++counter;
        }
    }

    public static ArrayList<ArrayList<Integer>> permutations = new ArrayList<>();

    public static boolean isForcingDirection(ArrayList<Integer> arr) {
        // is one before two: (1, 2, 3) --> ok
        // (3, 1, 2) --> ok
        // (3, 2, 1) --> not ok

        for(int i = 0; i < arr.size(); ++i) {
            if(arr.get(i) == 1)
                return true;
            if(arr.get(i) == 2)
                return false;
        }
        return true;
    }

    public static void generateUniquePermutations(ArrayList<Integer> arr) {
        Integer last = arr.isEmpty() ? null : arr.get(arr.size() - 1);
        arr.remove(arr.size() - 1);

        permute(arr, 0);

        // check if are forcing direction O(n!)
        Iterator<ArrayList<Integer>> iterator = permutations.iterator();
        while (iterator.hasNext()) {
            var perm = iterator.next();
            if (!isForcingDirection(perm)) {
                iterator.remove();
            }
        }

        //ad last node to permutation
        for(var perm : permutations) {
            perm.add(last);
        }

        System.out.println(Arrays.toString(permutations.toArray()));

    }

    public static boolean comparePermutations(ArrayList<Integer> perm1, ArrayList<Integer> perm2) {
        int n = perm1.size();
        for(int i = 0; i < n; ++i) {
            boolean isRepForward = true, isRepBack = true;
            for(int j = i, k = 0; k < perm1.size(); ++k, j = (j + 1)%n) {
                if(perm1.get(j) != perm2.get(k)) {
                    isRepForward = false;
                }

                int z = (((-j % n) + n) % n); //backwards perm
                if(perm1.get(z) != perm2.get(k)) {
                    isRepBack = false;
                }

                if(!isRepForward && !isRepBack)
                    break;
            }
            if(isRepForward || isRepBack) {
                notUniquePermutations.add(perm1);
                notUniquePermutations.add(perm2);
                return false;
            }
        }
        return true;
    }

    public static boolean comparePermutations() {
        for(int i = 0; i < permutations.size(); ++i) {
            for(int j = i + 1; j < permutations.size(); ++j) {
                if(!comparePermutations(permutations.get(i), permutations.get(j)))
                    return false;
            }
        }
        return true;
    }



    public static void main(String[] args) {

        generateUniquePermutations(new ArrayList<>(List.of(1, 2, 3, 4, 5, 6)));

        var areUnique = comparePermutations();


    }
}

