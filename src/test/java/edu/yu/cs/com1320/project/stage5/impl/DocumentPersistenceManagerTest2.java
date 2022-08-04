package edu.yu.cs.com1320.project.stage5.impl;

import org.junit.jupiter.api.Test;

import java.net.URI;

import static org.junit.jupiter.api.Assertions.*;

class DocumentPersistenceManagerTest2 {
    URI u1= URI.create("http://edu.yu.cs/com1320/project/doc2");
    URI u2= URI.create("mailto:jgjgh/gjgjk/fkgk.json");
    @Test
    public void test()
    {
        System.out.println(u1.getSchemeSpecificPart());
        System.out.println(u2.getSchemeSpecificPart());
    }


}