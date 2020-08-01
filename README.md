# RTS Labs Coding Challenge

## Questions 1 & 2 solved in [src/Problems.java](https://github.com/pwamsley2015/rts_labs_challenge)

## Question 3: If you could change 1 thing about your favorite framework/language/platform (pick one), what would it be?

My favorite language is Java. I’ll admit I have some biases—Java was the first programing language I learned, and I’ve built my coolest projects using Java. So it’s probably not surprising that I sympathize with the popular argument that Java’s static typing and general verbosity leads to less errors (or at least errors that are noticed and understood sooner), easier abstractions, and more readable code. With that said, there are certainly cases where Java forces the developer to roll their eyes as they type out several lines of code to express one simple idea. My one change to Java would be a solution to, in my view, one of the most frustrating instances of this gratuitous verbosoity. Let's take a look at some code I recently wrote on a personal project: 

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

I am not a fan of Java Exception Handleing in the case where a variable is being declared and assigned to a value. Here are my issues: 

1. The variable declaration and intended assignment must be seperated. 
1. The compiler won't allow reference to the variable after the try/catch if it was *potentially* never initalized, even if proper exception handling mades this impossible. To me, it feels rather ridicolous that `Object var;` won't work, but `Object var = null;` will.
1. Wrapping assignment in a try/catch block introduces two new scopes, which seems overkill when we're just trying to assign a variable. 
1. This situation often occurs when I don't know that my assignment requries exception handling. So I'll write something like: ```FirebaseOptions options = new FirebaseOptions(...).```. Then my IDE will kindly let me know that I need a try/catch, and even automatically generate it for me: 

```java
FirebaseOptions options;
try {
	options = new FirebaseOptions(...)
} catch(IOException e) {
	e.printStackTrace();
}
```

Now we're back at problem #2. So I need to go back, add the `= null;`, and then get back to using the new variable. These interuptions may be trivial, but that's exactly what makes them so frustating. 

All these issues can be addressed by my proposal: A one-liner ```java tryNew(Object o, ExceptionHandler<? extends Exception> h)``` method (which could live somewhere like the Objects class) which internally handle the exceptional flow control. Here's what that would look like:

```java
FirebaseOptions options = Objects.tryNew(new FirebaseOptions(...), (e -> e.printStackTrace()));
...
someMethod(options);
someOtherMethod(options.anotherMethod());
```
If our situation requries more complex error handling, that's nicely taken care of by the lambda expression, rather than a catch block.

```java
FirebaseOptions options = Objects.tryNew(new FirebaseOptions(...), (e -> { 
								somethingWentWrong();
								alertMissionControl();
								e.printStackTrace();
							})
	);
...
someMethod(options);
someOtherMethod(options.anotherMethod());
```
 
With this approach, exceptional flow control is abstracted away from the *use* of newly contructed objects, without losing any ability to do error handling. No new scopes are introduced *unless* we need one for error handling, and the new object can be used right away without null checks. 