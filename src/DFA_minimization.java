import java.lang.reflect.Array;
import java.util.*;

public class DFA_minimization {
    public static void main(String[] args) {
        long start = System.nanoTime();

        String[] states = {"a","b","c","d","e","f"};
        String[] values = {"0","1"};
        String[] finalStates = {"c","d","e"};
        String initial = "a";
        Arrays.sort(finalStates);
        String[][] Tran = {{"b","c"},{"a","d"},{"e","f"},{"e","f"},{"e","f"},{"f","f"}};


//        String[] states = {"A","B","C","D","E"};
//        String[] values = {"0","1","2"};
//        String[] finalStates = {"A","B","C","D"};
//        String initial = "A";
//        Arrays.sort(finalStates);
//        String[][] Tran = {{"B","C","D"},{"B","C","D"},{"E","C","D"},{"E","E","D"},{"E","E","E"}};

//        String[] states = {"a","b","c","d","e"};
//        String[] values = {"0","1"};
//        String[] finalStates = {"e"};
//        String initial = "a";
//        Arrays.sort(finalStates);
//        String[][] Tran = {{"b","c"},{"d","b"},{"b","c"},{"b","e"},{"b","c"}};
        outputNFA(states,values,finalStates,Tran);
        boolean[][] filling = new boolean[Tran.length][Tran.length];
        tableFilling(states,values,Tran,filling,finalStates,initial);
        long end = System.nanoTime();
        System.out.println("\nThe run time is "+(end-start)+" nanoseconds");
    }




    private static void tableFilling(String[] S, String[] V, String[][] T, boolean[][] fill, String[] F, String Q) {
        boolean flag = false;
        boolean main = true;
        ArrayList<String> mini = new ArrayList<String>();
        //pair final and non-final
        for(int i = 1; i < T.length; i++)
        {
            for(int j = 0; j < i; j++)
            {
                if((Arrays.binarySearch(F,S[i]) >= 0 && Arrays.binarySearch(F,S[j]) < 0)
                || (Arrays.binarySearch(F,S[i]) < 0 && Arrays.binarySearch(F,S[j]) >= 0))
                {
                    fill[i][j] = true;
                }
            }
        }

        do
        {
            main = true;
            for(int i = 1; i < T.length; i++)
            {
                for(int j = 0; j < i; j++)
                {
                    if(!fill[i][j])
                    {
                        for(int k = 0; k < V.length; k++)
                        {
                            int a = Arrays.binarySearch(S,T[i][k]);

                            int b = Arrays.binarySearch(S,T[j][k]);

                            if(fill[a][b] || fill[b][a])
                            {
                                fill[i][j] = true;
                                main = false;
                                break;
                            }
                        }

                    }
                }
            }
        }while (!main);

        for(int i = 1; i < T.length; i++)
        {
            for (int j = 0; j < i; j++)
            {
                if(!fill[i][j])
                {
                    String temp = S[j]+S[i];

                    if(mini.size() > 1)
                    {
                        for(int l = 0; l < mini.size(); l++)
                        {
                            if(intersect(mini.get(l),temp))
                            {
                                String haha = mini.get(l);
                                mini.remove(l);
                                mini.add(l, union(haha, temp));
                            }
                        }
                    }
                    else
                        mini.add(temp);
                }
            }
        }

        finalTransition(mini, T, S, V, F, Q);
    }


    private static void finalTransition(ArrayList<String> M, String[][] T, String[] S, String[] V, String[] F, String Q){
        boolean[] check = new boolean[S.length];
        System.out.println("\nMinimized Result");
        String con = "";
        boolean flag = false;
        for(int i = 0; i < M.size(); i++)
        {

            for(int k = 0; k < V.length; k++)
            {
                flag = false;
                con = "";
                for (int j = 0; j < M.get(i).length(); j++)
                {
                    String temp = String.valueOf(M.get(i).charAt(j));

                    int a = Arrays.binarySearch(S, temp);

                    con = union(con,T[a][k]);

                }
                for(int l = 0; l < M.size(); l++)
                {
                    if(M.get(l).contains(stringSort(con)))
                    {
                        System.out.println(M.get(i)+" , "+V[k]+" , "+M.get(l));
                        flag = true;
                    }
                }
                if(!flag)
                    System.out.println(M.get(i)+" , "+V[k]+" , other");
            }
        }
        for(int i = 0; i < V.length; i++)
        {
            System.out.println("other , "+V[i]+" , other");
        }

        //find other states
        for(int i = 0; i < M.size(); i++)
        {
            for(int j = 0; j < M.get(i).length(); j++)
            {
                String ff = String.valueOf(M.get(i).charAt(j));
                check[Arrays.binarySearch(S,ff)] = true;
            }
        }


        ArrayList<String> finalState = new ArrayList<String>();
        ArrayList<String> initialState = new ArrayList<String>();
        for(int i = 0; i < M.size(); i++)
        {
            for(int j = 0; j < F.length; j++)
            {
                if(M.get(i).contains(F[j]))
                {
                    finalState.add(M.get(i));break;
                }
            }
        }

        for(int i = 0; i < M.size(); i++)
        {
            if(M.get(i).contains(Q));
            {
                initialState.add(M.get(i));break;
            }

        }

        for(int i = 0; i < check.length; i ++)
        {
            if(!check[i])
            {
                if(Arrays.binarySearch(F,S[i]) >= 0)
                {
                    finalState.add("others");break;
                }
            }
        }

        List<String> states =  Arrays.asList(S);
        List<String> values =  Arrays.asList(V);
        System.out.println("\nS = "+states);
        System.out.println("\u2211 = "+ values);
        System.out.println("q0 = "+initialState);
        System.out.println("F = "+finalState);
    }




    private static boolean intersect(String s1, String s2)
    {
        HashSet<Character> h1 = new HashSet<Character>(), h2 = new HashSet<Character>();
        for(int i = 0; i < s1.length(); i++)
        {
            h1.add(s1.charAt(i));
        }
        for(int i = 0; i < s2.length(); i++)
        {
            h2.add(s2.charAt(i));
        }
        h1.retainAll(h2);
        String res = h1.toString();
        if(res == "[]")
            return false;
        else
            return true;
    }



    public static String union (String s1, String s2)
    {
        if (s1 == null)
            return s2;

        if (s2 == null)
            return s1;

        Set<String> unique = new LinkedHashSet<>();

        for (String word : s1.split(""))
        {
            word = word.trim();
            unique.add(word);
        }

        for (String word : s2.split(""))
        {
            word = word.trim();
            unique.add(word);
        }

        String ret = unique.toString().replaceAll("[\\[\\] ]", "");
        ret = ret.replaceAll("[,\\/]","");
        ret = stringSort(ret);
        return ret;
    }


    private static String stringSort(String s)
    {
        char temp[] = s.toCharArray();
        Arrays.sort(temp);
        s = new String(temp);
        return s;
    }



    private static void output(boolean[][] aa, String[][] T)
    {
        for(int i = 1; i < T.length; i++)
        {
            for(int j = 0; j < i; j++)
            {
                System.out.print(aa[i][j]+" ");
            }
            System.out.println();
        }
    }


    static void outputNFA(String[] S, String[] V, String[] F,String[][] T) {
        System.out.print("States\t");
        for(int a = 0; a < V.length; a++){
            System.out.print(V[a]+"\t\t");
        }
        System.out.println();

        for(int j = 0; j < T.length; j++)
        {
            System.out.print(S[j]);
            for(int k =0; k < T[j].length; k++)
            {
                System.out.print("\t\t"+T[j][k]);
            }
            System.out.println();
        }
    }
}