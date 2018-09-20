import tester.Tester;

interface ILoXMLFrag {
  int contentLength();

  boolean hasTag(String name);

  boolean hasAttribute(String name);

  boolean hasAttributeInTag(String tagName, String attName);

  ILoXMLFrag updateAttribute(String name, String value);

  String renderAsString();

}

class MtLoXMLFrag implements ILoXMLFrag {
  public int contentLength() {
    return 0;
  }

  //does object contain a tag with given name?
  public boolean hasTag(String name) {
    return false;
  }

  //does object contain an attribute with given name?
  public boolean hasAttribute(String name) {
    return false;
  }

  //does object contain an attribute with given name inside of a tag of a given name?
  public boolean hasAttributeInTag(String tagName, String attName) {
    return false;
  }

  //update all instances of attribute with given name to the given value
  public ILoXMLFrag updateAttribute(String name, String value) {
    return this;
  }

  public String renderAsString() {
    return "";
  }

}

class ConsLoXMLFrag implements ILoXMLFrag {
  //Fields
  IXMLFrag first;
  ILoXMLFrag rest;

  //Constructor
  ConsLoXMLFrag(IXMLFrag first, ILoXMLFrag rest) {
    this.first = first;
    this.rest = rest;
  }

  //how many total characters in the objects within this list?
  public int contentLength() {
    return this.first.contentLength() + this.rest.contentLength();
  }

  //does object contain a tag with given name?
  public boolean hasTag(String name) {
    return this.first.hasTag(name)
            || this.rest.hasTag(name);
  }

  //does object contain an attribute with given name?
  public boolean hasAttribute(String name) {
    return this.first.hasAttribute(name)
            || this.rest.hasAttribute(name);
  }

  //does object contain an attribute with given name inside of a tag of a given name?
  public boolean hasAttributeInTag(String tagName, String attName) {
    return this.first.hasAttributeInTag(tagName, attName)
            || this.rest.hasAttributeInTag(tagName, attName);
  }

  public String renderAsString() {
    if (this.rest.renderAsString().equals("")) {
      return this.first.renderAsString() + this.rest.renderAsString();
    }
    else {
      return this.first.renderAsString() + this.rest.renderAsString();
    }

  }

  public ILoXMLFrag updateAttribute(String name, String value) {
    return new ConsLoXMLFrag(this.first.updateAttribute(name, value),
            this.rest.updateAttribute(name, value));
  }
}

interface IXMLFrag {
  int contentLength();

  boolean hasTag(String name);

  boolean hasAttribute(String name);

  boolean hasAttributeInTag(String tagName, String attName);

  String renderAsString();

  IXMLFrag updateAttribute(String name, String value);
}

class Plaintext implements IXMLFrag {
  String txt;

  Plaintext(String txt) {
    this.txt = txt;
  }

  public int contentLength() {
    return this.txt.length();
  }

  public boolean hasTag(String name) {
    return false;
  }

  public boolean hasAttribute(String name) {
    return false;
  }

  public boolean hasAttributeInTag(String tagName, String attName) {
    return false;
  }

  public String renderAsString() {
    return this.txt;
  }

  public IXMLFrag updateAttribute(String name, String value) {
    return this;
  }
}

class Tagged implements IXMLFrag {
  Tag tag;
  ILoXMLFrag content;

  Tagged(Tag tag, ILoXMLFrag content) {
    this.tag = tag;
    this.content = content;
  }

  public int contentLength() {
    return this.content.contentLength();
  }

  public boolean hasTag(String name) {
    return this.tag.hasTag(name)
            || this.content.hasTag(name);
  }

  public boolean hasAttribute(String name) {
    return this.tag.hasAttribute(name)
            || this.content.hasAttribute(name);
  }

  public boolean hasAttributeInTag(String tagName, String attName) {
    return this.tag.hasAttributeInTag(tagName, attName)
            || this.content.hasAttributeInTag(tagName, attName);
  }

  public String renderAsString() {
    return this.content.renderAsString();
  }

  public IXMLFrag updateAttribute(String name, String value) {
    return new Tagged(this.tag.updateAttribute(name, value),
            this.content.updateAttribute(name, value));
  }
}

class Tag {
  String name;
  ILoAtt atts;

  Tag(String name, ILoAtt atts) {
    this.name = name;
    this.atts = atts;
  }

  boolean hasTag(String name) {
    return this.name.equals(name);
  }

  boolean hasAttribute(String name) {
    return this.atts.hasAttribute(name);
  }

  boolean hasAttributeInTag(String tagName, String attName) {
    return this.name.equals(tagName) && this.atts.hasAttribute(attName);
  }

  Tag updateAttribute(String name, String value) {
    return new Tag(this.name, this.atts.updateAttribute(name, value));
  }
}




// List of Attributes implementation
interface ILoAtt {
  boolean hasAttribute(String name);

  ILoAtt updateAttribute(String name, String value);
}

class ConsLoAtt implements ILoAtt {
  Att first;
  ILoAtt rest;

  ConsLoAtt(Att first, ILoAtt rest) {
    this.first = first;
    this.rest = rest;
  }

  public boolean hasAttribute(String name) {
    return this.first.hasAttribute(name)
            || this.rest.hasAttribute(name);
  }

  public ILoAtt updateAttribute(String name, String value) {
    return new ConsLoAtt(this.first.updateAttribute(name, value),
            this.rest.updateAttribute(name, value));
  }
}

class MtLoAtt implements ILoAtt {

  public boolean hasAttribute(String name) {
    return false;
  }

  public ILoAtt updateAttribute(String name, String value) {
    return this;
  }
}


class Att {
  String name;
  String value;

  Att(String name, String value) {
    this.name = name;
    this.value = value;
  }

  public boolean hasAttribute(String name) {
    return this.name.equals(name);
  }

  public Att updateAttribute(String name, String value) {
    if (this.hasAttribute(name)) {
      return new Att(name, value);
    }
    else {
      return this;
    }
  }
}



//Examples of XML
class ExamplesXML {
  Att att1 = new Att("volume", "30db");
  Att att2 = new Att("duration", "5sec");
  Att att3 = new Att("release", "2sec");
  Att att4 = new Att("force", "500N");
  Att att5 = new Att("height", "6ft");
  Att att6 = new Att("hangtime", "2.6sec");
  Att att7 = new Att("volume", "somethingRidiculous");

  ILoAtt loatt1 = new ConsLoAtt(att1, new ConsLoAtt(att2, new ConsLoAtt(att3, new MtLoAtt())));
  ILoAtt loatt2 = new ConsLoAtt(att4, new ConsLoAtt(att5, new ConsLoAtt(att6, new MtLoAtt())));
  ILoAtt loatt3 = new ConsLoAtt(att1, new MtLoAtt());
  ILoAtt loatt4 = new ConsLoAtt(att1, new ConsLoAtt(att2, new MtLoAtt()));
  ILoAtt loatt5 = new ConsLoAtt(att7, new ConsLoAtt(att2, new MtLoAtt()));


  Tag tag1 = new Tag("yell", loatt1);
  Tag tag2 = new Tag("smack", loatt2);
  IXMLFrag plaintext1 = new Plaintext("Alex Perez");  // length 10
  IXMLFrag plaintext2 = new Plaintext("bleep bloop"); // length 11
  IXMLFrag plaintext3 = new Plaintext("I am ");
  IXMLFrag plaintext4 = new Plaintext("XML");
  IXMLFrag plaintext5 = new Plaintext("!");
  IXMLFrag plaintext6 = new Plaintext("XM");
  IXMLFrag plaintext7 = new Plaintext("L");
  IXMLFrag plaintext8 = new Plaintext("I am XML!");
  IXMLFrag plaintext9 = new Plaintext("X");
  IXMLFrag plaintext10 = new Plaintext("ML");


  Tag tag3 = new Tag("yell", new MtLoAtt());
  Tag tag4 = new Tag("italic", new MtLoAtt());
  Tag tag5 = new Tag("yell", loatt3);
  Tag tag6 = new Tag("yell", loatt4);
  Tag tag7 = new Tag("yell", loatt5);

  Tagged tagged3 = new Tagged(tag3, new ConsLoXMLFrag(
          new Tagged(tag4, new ConsLoXMLFrag(
                  plaintext9, new MtLoXMLFrag())),
          new MtLoXMLFrag()));

  ILoXMLFrag xml1 = new ConsLoXMLFrag(plaintext8, new MtLoXMLFrag());

  ILoXMLFrag xml2 = new ConsLoXMLFrag(plaintext3,
          new ConsLoXMLFrag(new Tagged(tag3,
                  new ConsLoXMLFrag(plaintext4, new MtLoXMLFrag())),
                  new ConsLoXMLFrag(plaintext5, new MtLoXMLFrag())));

  ILoXMLFrag xml3 = new ConsLoXMLFrag(plaintext3,
          new ConsLoXMLFrag(
                  new Tagged(tag3,
                          new ConsLoXMLFrag(
                                  new Tagged(tag4,
                                          new ConsLoXMLFrag(plaintext9, new MtLoXMLFrag())),
                                  new ConsLoXMLFrag(plaintext10, new MtLoXMLFrag()))),
                  new ConsLoXMLFrag(plaintext5, new MtLoXMLFrag())));

  ILoXMLFrag xml4 = new ConsLoXMLFrag(plaintext3,
          new ConsLoXMLFrag(
                  new Tagged(tag5,
                          new ConsLoXMLFrag(
                                  new Tagged(tag4,
                                          new ConsLoXMLFrag(plaintext9, new MtLoXMLFrag())),
                                  new ConsLoXMLFrag(plaintext10, new MtLoXMLFrag()))),
          new ConsLoXMLFrag(plaintext5, new MtLoXMLFrag())));

  ILoXMLFrag xml5 = new ConsLoXMLFrag(plaintext3,
          new ConsLoXMLFrag(
                  new Tagged(tag6,
                          new ConsLoXMLFrag(
                                  new Tagged(tag4,
                                          new ConsLoXMLFrag(plaintext9, new MtLoXMLFrag())),
                                  new ConsLoXMLFrag(plaintext10, new MtLoXMLFrag()))),
                  new MtLoXMLFrag()));

  boolean testContentLength(Tester t) {
    return t.checkExpect(xml1.contentLength(), 9)
            && t.checkExpect(xml2.contentLength(), 9)
            && t.checkExpect(xml3.contentLength(), 9); // 10 + 10 + 11 + 10
  }

  boolean testHasTag(Tester t) {
    return t.checkExpect(xml1.hasTag("yell"), false)
            && t.checkExpect(xml2.hasTag("yell"), true)
            && t.checkExpect(xml3.hasTag("smack"), false)
            && t.checkExpect(xml3.hasTag("whoop"), false)
            && t.checkExpect(xml4.hasTag("italic"), true);
  }

  boolean testHasAttribute(Tester t) {
    return t.checkExpect(xml1.hasAttribute("hangtime"), false)
            && t.checkExpect(xml2.hasAttribute("volume"), false)
            && t.checkExpect(xml2.hasAttribute("height"), false)
            && t.checkExpect(xml3.hasAttribute("duration"), false)
            && t.checkExpect(xml3.hasAttribute("force"), false)
            && t.checkExpect(xml3.hasAttribute("boop"), false);
  }

  boolean testHasAttributeInTag(Tester t) {
    return t.checkExpect(xml1.hasAttributeInTag("yell", "volume"),
            false)
            && t.checkExpect(xml2.hasAttributeInTag("smack", "force"),
            false)
            && t.checkExpect(xml2.hasAttributeInTag("yell", "volume"),
            false)
            && t.checkExpect(xml3.hasAttributeInTag("yell", "duration"),
            false)
            && t.checkExpect(xml3.hasAttributeInTag("jump", "hangtime"),
            false)
            && t.checkExpect(xml4.hasAttributeInTag("yell", "volume"), true);
  }

  boolean testRenderAsString(Tester t) {
    return t.checkExpect(xml1.renderAsString(), "I am XML!")
            && t.checkExpect(xml2.renderAsString(),"I am XML!")
            && t.checkExpect(xml3.renderAsString(), "I am XML!");
  }

  boolean testUpdateAttribute(Tester t) {
    return t.checkExpect(xml2.updateAttribute("volume", "somethingRidiculous"), xml2)
            && t.checkExpect(xml5.updateAttribute("volume", "somethingRidiculous"),
            new ConsLoXMLFrag(plaintext3,
                    new ConsLoXMLFrag(
                            new Tagged(tag7,
                                    new ConsLoXMLFrag(
                                            new Tagged(tag4,
                                                    new ConsLoXMLFrag(plaintext9,
                                                            new MtLoXMLFrag())),
                                            new ConsLoXMLFrag(plaintext10, new MtLoXMLFrag()))),
                            new MtLoXMLFrag())));
  }
}