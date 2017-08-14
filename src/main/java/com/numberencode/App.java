package com.numberencode;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class App {
    //Loaded only once when the program is started (Improves Performance)
    static List<String> dictionary = new ArrayList<>();

    public static void main(String[] args) {

        try{
            String word;
            BufferedReader dictionaryFile = new BufferedReader(new FileReader("input/dictionary.txt"));

            while((word = dictionaryFile.readLine()) != null) {
                dictionary.add(word);
            }
            Collections.sort(dictionary);//This will help in applying Binary Search later in the code to reduce number of iterations.


            String line;
            BufferedReader inputFile = new BufferedReader(new FileReader("input/input.txt"));
            while((line = inputFile.readLine()) != null) {
                processMessage(line.trim());//Takes care of trailing spaces
            }
        }

        catch (IOException e){
            e.printStackTrace();
        }

    }

    /**
     * Used to process the phone number and remove unwanted characters like "/" and "-"
     * @param rawPhoneNumber
     */
    public static void processMessage(String rawPhoneNumber){
        if(rawPhoneNumber != null && !rawPhoneNumber.isEmpty() ){

            String[] test = rawPhoneNumber.split("");
            int[] numbers = new int[test.length];

            int index = 0;
            for(int i = 0;i < test.length;i++)
            {
                try
                {
                    numbers[index] = Integer.parseInt(test[i]);
                    index++;
                }
                catch (NumberFormatException nfe)
                {
                    //In case of any unwanted character (Do Nothing)
                }
            }
            numbers = Arrays.copyOf(numbers, index);//This process removed the unwanted characters and now we have a clean phone number.
            generateCombinations(numbers,rawPhoneNumber);

        }

    }

    /**
     * Wrapper method to generate the different combinations which can be made by one phone number.
     * @param numbers
     * @param message
     */
    private static void generateCombinations(int [] numbers, String message){
        int n = numbers.length;
        char result[] = new char[n+1];
        result[n] ='\0';
        generateCombinationsUtil(numbers, 0, result, n,message);

    }


    /**
     * Used to encode phone numbers to Words and match it with the disctionary.
     * @param number
     * @param current_digit
     * @param output
     * @param n
     * @param phoneNumber
     */
    private static void generateCombinationsUtil(int number[], int current_digit, char output[], int n, String phoneNumber){

        //Base case, if current word is prepared
        int i=0;
        if (current_digit == n)
        {
            String b = new String(output).replaceAll("\\W","");
            //Conducts Binary Search (Improves performance O(logN) complexity)
            int a = Collections.binarySearch(dictionary,b);
            //Index positive means that a valid word exists in the disctionary
            if(a>=0){
                printWordAndPhoneNumber(b,phoneNumber);
            }
            return;
        }


        while(current_digit<n) {
            while(i<Constant.phoneNumberHashTable[number[current_digit]].length())
            {
                output[current_digit] = Constant.phoneNumberHashTable[number[current_digit]].charAt(i);
                generateCombinationsUtil(number, current_digit+1, output, n,phoneNumber);
                i++;
            }
            current_digit++;
        }


    }

    private static void printWordAndPhoneNumber (String encodedWord, String phoneNumber){
        System.out.println(phoneNumber+": "+encodedWord);
    }

}
