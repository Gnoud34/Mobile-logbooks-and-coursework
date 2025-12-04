import 'package:flutter/material.dart';
import '../db/database_helper.dart';
import '../models/hike.dart';

class AddHikeScreen extends StatefulWidget {
  final Hike? initial;
  const AddHikeScreen({super.key, this.initial});

  @override
  State<AddHikeScreen> createState() => _AddHikeScreenState();
}

class _AddHikeScreenState extends State<AddHikeScreen> {
  final _form = GlobalKey<FormState>();
  final _ctrl = <String, TextEditingController>{
    'name': TextEditingController(),
    'location': TextEditingController(),
    'length': TextEditingController(),
    'description': TextEditingController(),
    'weather': TextEditingController(),
    'terrain': TextEditingController(),
  };
  String _parking = 'Yes';
  String _difficulty = 'Easy';
  DateTime? _date;

  @override
  void initState() {
    super.initState();
    final h = widget.initial;
    if (h != null) {
      _ctrl['name']!.text = h.name;
      _ctrl['location']!.text = h.location;
      _ctrl['length']!.text = h.length;
      _ctrl['description']!.text = h.description ?? '';
      _ctrl['weather']!.text = h.weather ?? '';
      _ctrl['terrain']!.text = h.terrain ?? '';
      _parking = h.parking;
      _difficulty = h.difficulty;
      _date = DateTime.tryParse(h.date);
    }
  }

  @override
  void dispose() {
    for (final c in _ctrl.values) {
      c.dispose();
    }
    super.dispose();
  }

  Future<void> _pickDate() async {
    final now = DateTime.now();
    final picked = await showDatePicker(
      context: context,
      initialDate: _date ?? now,
      firstDate: DateTime(2020),
      lastDate: DateTime(2030),
    );
    if (picked != null) {
      setState(() => _date = picked);
    }
  }

  Future<void> _save() async {
    if (!_form.currentState!.validate()) return;
    if (_date == null) {
      ScaffoldMessenger.of(context)
          .showSnackBar(const SnackBar(content: Text('Please pick a date')));
      return;
    }

    final data = Hike(
      id: widget.initial?.id,
      name: _ctrl['name']!.text.trim(),
      location: _ctrl['location']!.text.trim(),
      date: _date!.toIso8601String().split('T')[0], // YYYY-MM-DD
      parking: _parking,
      length: _ctrl['length']!.text.trim(),
      difficulty: _difficulty,
      description: _ctrl['description']!.text.trim().isEmpty
          ? null
          : _ctrl['description']!.text.trim(),
      weather: _ctrl['weather']!.text.trim().isEmpty
          ? null
          : _ctrl['weather']!.text.trim(),
      terrain: _ctrl['terrain']!.text.trim().isEmpty
          ? null
          : _ctrl['companions']!.text.trim(),
    );

    final db = await DatabaseHelper.instance.db;
    if (data.id == null) {
      await db.insert('hikes', data.toMap());
    } else {
      await db.update('hikes', data.toMap(),
          where: 'id=?', whereArgs: [data.id]);
    }
    if (!mounted) return;
    Navigator.pop(context);
  }

  InputDecoration _dec(String label) =>
      InputDecoration(labelText: label, border: const OutlineInputBorder());

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
          title: Text(widget.initial == null ? 'Add Hike' : 'Edit Hike')),
      body: Padding(
        padding: const EdgeInsets.all(12),
        child: Form(
          key: _form,
          child: ListView(
            children: [
              TextFormField(
                  controller: _ctrl['name'],
                  decoration: _dec('Name'),
                  validator: _req),
              const SizedBox(height: 10),
              TextFormField(
                  controller: _ctrl['location'],
                  decoration: _dec('Location'),
                  validator: _req),
              const SizedBox(height: 10),
              Row(
                children: [
                  Expanded(
                    child: Text(
                      _date == null
                          ? 'No date selected'
                          : 'Date: ${_date!.toLocal().toString().split(' ')[0]}',
                    ),
                  ),
                  ElevatedButton.icon(
                    icon: const Icon(Icons.calendar_month),
                    onPressed: _pickDate,
                    label: const Text('Pick Date'),
                  ),
                ],
              ),
              const SizedBox(height: 10),
              DropdownButtonFormField<String>(
                value: _parking,
                decoration: _dec('Parking'),
                items: const ['Yes', 'No']
                    .map((e) =>
                    DropdownMenuItem(value: e, child: Text(e)))
                    .toList(),
                onChanged: (v) => setState(() => _parking = v!),
              ),
              const SizedBox(height: 10),
              TextFormField(
                  controller: _ctrl['length'],
                  decoration: _dec('Length'),
                  validator: _req),
              const SizedBox(height: 10),
              DropdownButtonFormField<String>(
                value: _difficulty,
                decoration: _dec('Difficulty'),
                items: const ['Easy', 'Normal', 'Hard', 'Very Hard']
                    .map((e) =>
                    DropdownMenuItem(value: e, child: Text(e)))
                    .toList(),
                onChanged: (v) => setState(() => _difficulty = v!),
              ),
              const SizedBox(height: 10),
              TextFormField(
                  controller: _ctrl['description'],
                  decoration: _dec('Description (optional)')),
              const SizedBox(height: 10),
              TextFormField(
                  controller: _ctrl['weather'],
                  decoration: _dec('Weather (optional)')),
              const SizedBox(height: 10),
              TextFormField(
                  controller: _ctrl['terrain'],
                  decoration: _dec('Terrain (optional)')),
              const SizedBox(height: 16),
              ElevatedButton.icon(
                  onPressed: _save,
                  icon: const Icon(Icons.save),
                  label: const Text('Save')),
            ],
          ),
        ),
      ),
    );
  }

  String? _req(String? v) =>
      (v == null || v.trim().isEmpty) ? 'Required' : null;
}
