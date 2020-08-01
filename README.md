# RTS Labs Coding Challenge

## Questions 1 & 2 solved in [src/Problems.java](https://github.com/pwamsley2015/rts-labs-challenge/blob/master/src/Problems.java)

## Question 3: If you could change 1 thing about your favorite framework/language/platform (pick one), what would it be?

My favorite language is Java. I’ll admit I have some biases—Java was the first programing language I learned, and I’ve built my coolest projects using Java. So it’s probably not surprising that I sympathize with the popular argument that Java’s static typing and general verbosity leads to less errors (or at least errors that are noticed and understood sooner), easier abstractions, and more readable code. With that said, there are certainly cases where Java forces the developer to roll their eyes as they type out several lines of code to express one simple idea. My one change to Java would be a solution to a particularly frustrating instance of gratuitous verbosoity. Let's take a look at some code I recently wrote on a personal project: 

```java
FirebaseOptions options = null;
try {
	options = new FirebaseOptions.Builder()
				.setCredentials(GoogleCredentials.getApplicationDefault())
				.setDatabaseUrl(props.getDatabaseURL())
				.build();
} catch (IOException e) {
	e.printStackTrace();
}

...


if (options != null) {
	...
}
```

I am not a fan of Java Exception Handling in the case where a variable is being declared and assigned to a newly contructed object. Here are my issues: 

1. The variable declaration and intended assignment must be seperated. 
1. The compiler won't allow reference to the variable after the try/catch if it was *potentially* never initalized, even if proper exception handling makes this impossible. To me, it feels rather ridiculous that `Object var;` won't work, but `Object var = null;` will.
1. Wrapping assignment in a try/catch block introduces two new scopes, which seems overkill when we're just trying to assign a variable. 
1. This situation often occurs when a developer doesn't know that the assignment requires exception handling. So, they'll write something like: ```FirebaseOptions options = new FirebaseOptions(...)```. Then their IDE will kindly let them know that they need a try/catch, and even automatically generate it: 

```java
FirebaseOptions options;
try {
	options = new FirebaseOptions(...)
} catch(IOException e) {
	e.printStackTrace();
}
```

 Now we're back at problem #2. So they need to go back, add the `= null;`, and then get back to using the new object. These interruptions may be trivial, but that's exactly what makes them so frustating. 

All these issues can be addressed by my proposal: A one-liner method ```tryNew(Object o, ExceptionHandler<Exception> h)```  (that could live somewhere like the Objects class) which internally handles exceptional flow control. This approach changes the code to:

```java
FirebaseOptions options = Objects.tryNew(new FirebaseOptions(...), (e -> e.printStackTrace()));
...
```
If our situation requries more complex error handling, that's nicely taken care of by the lambda expression, rather than a catch block.

```java
FirebaseOptions options = Objects.tryNew(new FirebaseOptions(...), (e -> { 
				somethingWentWrong();
				alertMissionControl(e);
				e.printStackTrace();
			})
	);
...
```
 
With this approach, exceptional flow control is abstracted away from the *use* of newly contructed objects, without losing any ability to do error handling. No new scopes are introduced **unless** we *actually* need one for error handling, and the new object can be used right away without a null check. 