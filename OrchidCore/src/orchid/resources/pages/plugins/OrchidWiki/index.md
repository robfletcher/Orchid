---
official: true
description: A Gitbook-like wiki for your Orchid site. 
images:
  - src: https://res.cloudinary.com/orchid/image/upload/c_scale,w_300,e_blur:150/v1524973072/plugins/wiki.jpg
    alt: Wiki
    caption: Photo by Patrick Tomasso on Unsplash
---

### Creating Wikis

An Orchid Wiki is a collection of pages structured in a freeform hierarchy. Pages in a wiki are all related and ordered, 
with one page linking directly to the next so a user can read the entire contents of the wiki from front-end simply by 
navigating the page links. Orchid Wikis also create Menu items which display the full wiki tree in an easily navigable
fashion.

Wikis in Orchid are inspired by GitBook and are set up in a similar manner. To start, add a file named `summary` (in 
your language of choice) to `wiki/`. This file becomes the landing page for the wiki and can contain any kind of 
content, which gets displayed exactly as written. But it also gets converted to HTML and all links in this file become a 
page in the wiki. 

For example: 

{% highlight 'html' %}
### Wiki Heading

[Getting Started](getting-started.md)
[Basic Setup](setup/basic.md)
[Advanced Setup](setup/advanced.md)
{% endhighlight %}

corresponds to the following directory structure:

{% filter compileAs('uml') %}
@startsalt
{
{T
+ / (resources root)
++ config.yml
++ /wiki
+++ summary.md
+++ getting-started.md
+++ /setup
++++ basic.md
++++ advanced.md
}
}
@endsalt
{% endfilter %}


which produces the following output:

{% filter compileAs('uml') %}
@startsalt
{
{T
+ /wiki
++ index.html
++ /getting-started
+++ index.html
++ /setup
+++ /basic
++++ index.html
+++ /advanced
++++ index.html
}
}
@endsalt
{% endfilter %}

The content of those wiki pages can be anything, and can be written in any language as long as there is a Compiler for 
it (just like the summary, and any other page). Orchid also creates a new menu item type which links to every page in 
the wiki and is displayed recursively in the same hierarchy as the pages themselves. 

You can also customize the source directory of the wiki, and even set up multiple wiki sections which each have their 
own `summary` file, pages, and menu items. The following snippet should go in your site's `config.yml`:

{% highlight 'yaml' %}
wiki:
  baseDir: 'docs'  # (1) 
  sections:
    - 'userManual'  # (2)
    - 'developerGuide'  # (3)
{% endhighlight %}

1) Looks for the Wiki in /docs instead of /wiki
2) Creates a wiki based on {baseDir}/userManual/summary.md
3) Creates a wiki based on {baseDir}/developerGuide/summary.md