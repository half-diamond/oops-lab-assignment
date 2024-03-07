import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class QuizBowl {
    static ArrayList<QuestionMC> questionMC = new ArrayList<>();
    static ArrayList<QuestionSA> questionSA = new ArrayList<>();
    static ArrayList<QuestionTF> questionTF = new ArrayList<>();
    static ArrayList<Player> player_list = new ArrayList<>();

    static String input(Scanner in) {
        String iString = in.nextLine();
        while (iString.isBlank()) {
            System.out.println("Enter correct name !!");
            iString = in.nextLine();
        }
        return iString;
    }

    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        boolean flag = true;
        while (flag) {
            flag = false;
            System.out.println("What is your first name?");
            Player player = new Player();
            String input_String = input(in);
            player.firstname = input_String;
            System.out.println("What is your last name?");
            input_String = input(in);
            player.lastname = input_String;
            boolean flag0 = true;
            System.out.println("What file stores your questions?");
            while (flag0) {
                flag0 = false;
                input_String = in.nextLine();
                File file = new File(input_String);
                int no_of_ques = 0;
                try {
                    Scanner reader = new Scanner(file);
                    if (!reader.hasNextInt()) {

                        System.out.println("input file is not correct please enter a correct file");
                        continue;
                    }
                    no_of_ques = reader.nextInt();

                    int input_int = 0;
                    do {
                        System.out.println("How many questions would you like (out of " + no_of_ques + ")?");
                        boolean flag2 = true;
                        while (flag2) {
                            if (in.hasNextInt()) {
                                input_int = in.nextInt();
                                flag2 = false;
                            } else {
                                in.nextLine();
                                System.out.println("Enter integer value only");
                            }

                        }

                        if (input_int > no_of_ques)
                            System.out.println("Sorry, that is too many.");

                    } while (input_int > no_of_ques);

                    ArrayList<String> questions = new ArrayList<>();
                    extract_data_from_file(reader, questions);

                    Random rand = new Random();
                    boolean[] check = new boolean[no_of_ques];
                    in.nextLine();
                    
                    for (int i = 0; i < input_int; i++) {
                        int rand_int = rand.nextInt(no_of_ques);
                        while (check[rand_int]) {
                            rand_int = rand.nextInt(no_of_ques);
                        }
                        check[rand_int] = true;
                        String id = questions.get(rand_int);
                        String type = id.substring(0, 2);
                        int num = Integer.parseInt(id.substring(2));
                        play(type, in, num, player);
                        
                    }

                    player_list.add(player);
                    player_list.sort((b, a) -> a.points - b.points);
                    
                    System.out.println(
                        player.firstname + " " + player.lastname + ", your game is over!\nYou final score is "
                        + player.points + " points.\nBetter luck next time!");
                        System.out.println("your rank is " + (player_list.indexOf(player) + 1));
                        
                        for (int i = 1; i <= player_list.size(); i++) {
                            if (i - 1 == player_list.indexOf(player))
                            System.out.println(i + ")" + " " + player_list.get(i - 1).firstname + " "
                            + player_list.get(i - 1).lastname + "    <======");
                            else
                            System.out.println(i + ")" + " " + player_list.get(i - 1).firstname + " "
                            + player_list.get(i - 1).lastname);
                        }

                        while (flag == false) {
                            System.out.println("Enter 1 To keep playing or 2 To exit.");
                            String temp = in.nextLine();
                            if (temp.matches("1")) {
                                flag = true;
                            } else if (temp.matches("2")) {
                                return;
                            } else {
                                System.out.println("Enter 1 or 2 only!!");
                        }
                    }
                } catch (FileNotFoundException e) {
                    System.out.println("Please enter the path of correct file !!!");
                    flag0 = true;
                }
            }
        }
    }
    
    public static void extract_data_from_file(Scanner reader, ArrayList<String> questions) {
        int nmc = 0, ntf = 0, nsa = 0;
        reader.nextLine();
        while (reader.hasNextLine()) {
            String line1 = reader.nextLine();
            
            String type = line1.substring(0, 2);
            int point = Integer.parseInt(line1.substring(3));
            switch (type) {
                case "SA":
                QuestionSA objSA = new QuestionSA();
                objSA.ques = reader.nextLine();
                objSA.point = point;
                objSA.answer = reader.nextLine();
                questionSA.add(objSA);
                questions.add(type + nsa);
                    nsa++;
                    break;
                    
                    case "TF":
                    QuestionTF objTF = new QuestionTF();
                    objTF.ques = reader.nextLine();
                    objTF.point = point;
                    objTF.answer = reader.nextLine();
                    questionTF.add(objTF);
                    questions.add(type + ntf);
                    ntf++;
                    break;
                    
                    case "MC":
                    QuestionMC objMC = new QuestionMC();
                    objMC.ques = reader.nextLine();
                    objMC.point = point;
                    int options = reader.nextInt();
                    reader.nextLine();
                    
                    for (int i = 0; i < options; i++) {
                        objMC.options.add(reader.nextLine());
                    }
                    objMC.answer = reader.nextLine();
                    questionMC.add(objMC);
                    questions.add(type + nmc);
                    nmc++;
                    break;
                    
                    default:
                    
                    break;
                    
                }
            }
    }
    
    public static void play(String type, Scanner in, int num, Player player) {
        String ans;
        boolean flag = true;
        switch (type) {
            case "MC":
            QuestionMC q1 = questionMC.get(num);
            while (flag) {
                flag = false;
                char ch = 65;
                System.out.println(q1.ques+ " Points :- "+q1.point);
                for (String opt : q1.options) {
                    
                    System.out.println(ch + ") " + opt);
                    ch++;
                    
                }
                ans = in.nextLine();
                char cans = ans.toUpperCase().charAt(0);
                
                if (ans.toUpperCase().matches(q1.answer.toUpperCase())) {
                    
                    System.out.println("correct");
                    player.points += q1.point;
                    
                } else if (ans.toUpperCase().matches("SKIP")) {
                    
                    System.out.println("you have selected to skip the question");
                    return;
                    
                } else if (ans.length() != 1 || cans < 65 || cans > q1.options.size() + 64) {
                    
                    System.out.println("Enter your answer from A to " + (char) (64 + q1.options.size()));
                    flag = true;
                    
                } else {
                    
                    System.out.println("incorrect");
                    player.points -= q1.point;
                    
                }
            }
            break;
            
            case "SA":
            QuestionSA q2 = questionSA.get(num);
            System.out.println(q2.ques+" Points :- "+q2.point);
            ans = in.nextLine();
            if (ans.toUpperCase().matches(q2.answer.toUpperCase())) {
                
                System.out.println("correct");
                player.points += q2.point;
                
            } else if (ans.toUpperCase().matches("SKIP")) {

                    System.out.println("you have selected to skip the question");
                    return;

                } else {
                    
                    System.out.println("incorrect");
                    player.points -= q2.point;
                }
                break;
                
                case "TF":
                QuestionTF q3 = questionTF.get(num);
                while (flag) {
                    flag = false;
                    
                    System.out.println(q3.ques+"   Points :- "+q3.point);
                    ans = in.nextLine();
                    if (ans.toUpperCase().matches(q3.answer.toUpperCase())) {

                        System.out.println("correct");
                        player.points += q3.point;

                    } else if (ans.toUpperCase().matches("SKIP")) {

                        System.out.println("you have selected to skip the question");
                        return;

                    } else if (!(ans.toUpperCase().matches("TRUE") || ans.toUpperCase().matches("FALSE"))) {

                        System.out.println("Answer in true or false only");
                        flag = true;

                    } else {

                        System.out.println("incorrect");
                        player.points -= q3.point;
                    }
                }
                break;

            default:
                break;
        }
    }
}