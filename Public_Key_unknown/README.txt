Firts thing we will explain how to run the program successfully ...

When you run the program, you will be asked to enter your chice
( 1 for the first part, and 2 for the second part ).
after you chose the number, you will enter either n for the first part,
or the number of digits of n for the second part.
And then the program will do the rest, and you will find the decrypted
files in the same project folder.

-----------------------------------------------------------

Now for the strategy and alogrithems used ...

for the first part (p1):

we found p & q by the method findPrimePair(..) for the given n, as p & q to be primes.
then we get all possible e's based on that (q-1)*(p-1) should
be relativly prime with e.
after we got all possiple e's by the method findE's(...), we got all possible d's = 184000 (approximatly) by the method findD's(...).
then we checked the possible d's by the method checkD's(...) such that if there any integer bigger
than 78 we ignor this d. after this step, we got just 6 d's that 
can be used to decrypte (p1) file.
finally, after got the correct d, by the method decryption(...), we encrypt the file.
the run approximate time = 10 to 12 minutes.

-----------------------------------------------------------

for the second part (p2):

In this question we have duplicated some method due to some
slightly changes, like "findDs2(...)" and "decryption2(...)" to 
help us in finding the N's possibilties.

First Step: we had a method to find all the possibles N's which
has two multiply primes that given a product has same 
number of digit as in the question. 

Second Step: we pass each possible N to method called 
"findEs(...)" to find the possible E's for each N. 

Third Step: method "findEs(...)" will have an array of E's that 
could be correct so, we will pass each E to method called 
"findDs2(...)" to find the possible D's for each E, 
and if all E's didn't fit, then the program will go back and recieve 
new N to try it.

Forth Step: method "findDs2(...)" will use the possible D's to 
decrypt the massage, and if it's not correct will try the other E's 
to receive new D's.

the run approximate time = 3 to 4 hours