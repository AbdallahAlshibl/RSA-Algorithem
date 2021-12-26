The Object of ReadMe file is to explain how the program run and show all the steps that program require to run.

Step 1: The project have two class, they are "RSA Class" and "RSAMain Class". So their files ("RSA.java" and "RSAMain.java) must be downloaded in the compiler.

Step 2: You need to move your ".txt" files to the project's workspace for using them easily without write their path in the code.

Step 3: After having the ".txt" files in the workspace, then you should write the name of the ".txt" file in the RSAMain class in the variable   [ File file = new File("write_your_file_name_here.txt"); ]  you will find this variable in line 10.

Step 4: Make sure your ".txt" file have the message and the public key.

Step 5: Now you should run the program. And after you run the program there will be 2 operations done automaticlly:

Operation 1: convert the massage into integers.
Operation 2: Encrypt these integers and save the encrypted message in a new file called the same name as the previous file but with ".rsa" extension.

Step 6: In this step the compiler will ask the user to enter the private key values which they are "d" and "n", and you can get the private keys of the test cases in the file named "Private_keys for_3_test_cases.txt".

Step 7: Then after inserting the private key the program will create a new file called same name as the encyption file but with ".dec" extension.

Step 8: you should find the all the three files ".txt", ".rsa" and ".dec" in the workspace of the project.


Note: some times there may be an error if trying to run the third case (file t22.txt), the program may not work and send an error because of the compiler’s garbage space is full. You just need to empty the compiler’s garbage, and it will work fine.

That's the end.		
	
		
								Thanks...